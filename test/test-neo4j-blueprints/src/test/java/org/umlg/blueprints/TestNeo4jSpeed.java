package org.umlg.blueprints;

import com.tinkerpop.gremlin.neo4j.structure.Neo4jGraph;
import com.tinkerpop.gremlin.structure.Edge;
import com.tinkerpop.gremlin.structure.Vertex;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Date: 2013/12/26
 * Time: 2:06 PM
 */
public class TestNeo4jSpeed {

    Neo4jGraph graph;
    private Throwable toThrow;
    Random rand = new Random();

    @Before
    public void before() throws IOException {
        File f = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "neo4j-performance");
        if (f.exists()) {
            f.delete();
        }
        f.mkdir();
        graph = Neo4jGraph.open(f.getAbsolutePath());
    }


    @After
    public void after() throws Exception {
        graph.close();
    }

//    @Test
    public void testMultiThreadedCommits() throws Exception {
        for (int numThreads : new int[]{1, 2, 3, 4, 5, 10, 25, 50, 100, 150, 250, 500, 750, 1000}) {
            final int numVerticesPerThread = (numThreads <= 10 ? 10000 : (numThreads <= 100 ? 100000 : 100000)) / numThreads;
            int numElements = 2 * numVerticesPerThread * numThreads;

            ExecutorService service = Executors.newFixedThreadPool(numThreads);

            final Object[] startVertex = new Object[numThreads];

            final CountDownLatch cdl = new CountDownLatch(numThreads);
            long ts = System.currentTimeMillis();
            final String TEST_LABEL = "test";
            for (int i = 0; i < numThreads; i++) {
                final int tid = i;
                System.out.println("Scheduling write work for thread " + tid);
                service.submit(new Runnable() {
                    @Override
                    public void run() {
                        Object prevId = null;
                        for (int j = 0; j < numVerticesPerThread; j++) {
                            Vertex v = graph.addVertex("null");
                            if (prevId == null) {
                                startVertex[tid] = v.id();
                            } else {
                                Vertex prevV = graph.v(prevId);
                                prevV.addEdge(TEST_LABEL, v);
                            }
                            graph.tx().commit();

                            prevId = v.id();
                        }

                        System.out.println("Thread " + tid + " is done");
                        cdl.countDown();
                    }
                });
            }

            cdl.await();

            double duration = System.currentTimeMillis() - ts;
            System.out.println("Took " + duration + "ms to save " + numElements + " vertices+edges. Rate = " + (duration / numElements) + "ms per vertex. TPS = " + ((double) numElements * 1000 / duration));

            // Wait 10 seconds between tests
            Thread.sleep(10000);

//            ((BitsyGraph)graph).setDefaultIsolationLevel(BitsyIsolationLevel.READ_COMMITTED);
            final CountDownLatch cdl2 = new CountDownLatch(numThreads);
            ts = System.currentTimeMillis();
            for (int i = 0; i < numThreads; i++) {
                final int tid = i;
                System.out.println("Scheduling read work for thread " + tid);
                service.submit(new Runnable() {
                    @Override
                    public void run() {
                        for (int k = 0; k < 100; k++) {
                            int count = 0;
                            Vertex v = graph.v(startVertex[tid]);

                            Edge e;
                            do {
                                Iterator<Edge> eIter = v.outE();
                                if (!eIter.hasNext()) {
                                    break;
                                } else {
                                    count++;
                                    v = eIter.next().inV().next();
                                }
                            } while (true);

                            if (numVerticesPerThread != count + 1) {
                                System.out.println("Mistmatch between " + numVerticesPerThread + " and " + count);
                            }

                            graph.tx().commit();
                        }

                        System.out.println("Thread " + tid + " is done");
                        cdl2.countDown();
                    }
                });
            }

            cdl2.await();

            duration = System.currentTimeMillis() - ts;
            System.out.println("Took " + duration + "ms to query " + numElements + " vertices+edge 100 times. Rate = " + (duration / numElements) + "ms per vertex. TPS = " + ((double) numElements * 100000 / duration));
//            ((BitsyGraph)graph).setDefaultIsolationLevel(BitsyIsolationLevel.REPEATABLE_READ);

            service.shutdown();

            // Uncomment to look at memory usage
//            Thread.sleep(1000);
//            System.gc();
//            Thread.sleep(30000);

            // Clear graph
            after();
            before();
        }
    }

//    @Test
    public void testMultiThreadedReadsOnBipartiteGraph() throws Exception {
        final int numVertices = 1000000; // 100K vertices
        final int numIters = 100000;
        final int numElements = 8 * numIters; // expected to visit 8 v/e per iteration
        final int partSize = numVertices / 2;
        final int numPerCommit = 1000;

        final String label = "test";
        final Object[] outVertices = new Object[partSize];
        final Object[] inVertices = new Object[partSize];

        // Vertices
        for (int i=0; i < partSize; i++) {
            outVertices[i] = graph.addVertex("null").id();
            inVertices[i] = graph.addVertex("null").id();

            if (i % numPerCommit == 0) {
                graph.tx().commit();
            }
        }

        // Edges
        for (int i=0; i < partSize; i++) {
            Vertex outVertex = graph.v(outVertices[i]);
            outVertex.addEdge(label, graph.v(inVertices[(5 * i + 1) % partSize]));
            outVertex.addEdge(label, graph.v(inVertices[(5 * i + 4) % partSize]));
            outVertex.addEdge(label, graph.v(inVertices[(5 * i + 7) % partSize]));

            if (i % numPerCommit == 0) {
                graph.tx().commit();
            }
        }
        graph.tx().commit();

        final int numRuns = 3;
        Map<Integer, String> calcStrMap = new HashMap<Integer, String>();

        for (int run=0; run < numRuns; run++) {
//            ((BitsyGraph)graph).setDefaultIsolationLevel(BitsyIsolationLevel.READ_COMMITTED);
            for (final int numThreads : new int[] {1, 2, 3, 4, 5, 10, 25, 50, 100, 150, 250, 500, 750, 1000}) {
                ExecutorService service = Executors.newFixedThreadPool(numThreads);

                final CountDownLatch cdl = new CountDownLatch(numThreads);
                long ts = System.currentTimeMillis();

//                ((BitsyGraph)graph).setDefaultIsolationLevel(BitsyIsolationLevel.READ_COMMITTED);
                System.out.println("Running bi-partite read test with " + numThreads + " threads");
                for (int i = 0; i < numThreads; i++) {
                    final int tid = i;
                    //System.out.println("Scheduling read work for thread " + tid);
                    service.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Vertex v = graph.v(outVertices[0]);
                                long startTime = System.currentTimeMillis();

                                for (int k=0; k < 100 * numIters / numThreads; k++) {
                                    Assert.assertNotNull(v);

                                    Vertex nextV = randomVertex(v.out(label).toList());

                                    Assert.assertNotNull(nextV);

                                    // Take a random edge back
                                    Vertex backV = randomVertex(nextV.out().toList());
                                    if (backV != null) {
                                        v = backV;
                                    }

                                    if (k % 1000000 == 0) {
                                        long endTime = System.currentTimeMillis();
                                        System.out.println(endTime - startTime);
                                        startTime = endTime;
                                    }

                                }

                                //System.out.println("Thread " + tid  + " is done");
                            } catch (Throwable t) {
                                setException(t);
                            } finally {
                                cdl.countDown();
                            }
                        }

                        private Vertex randomVertex(Iterable<Vertex> vertices) {
                            List<Vertex> options = new ArrayList<Vertex>();
                            for (Vertex option : vertices) {
                                options.add(option);
                            }

                            if (options.isEmpty()) {
                                return null;
                            } else {
                                return options.get(rand.nextInt(options.size()));
                            }
                        }
                    });
                }

                cdl.await();

                if (getException() != null) {
                    throw new RuntimeException("Error in testMultiThreadedReadsOnBipartiteGraph", getException());
                }

                service.shutdown();

                long duration = System.currentTimeMillis() - ts;
                double tps = ((double)numElements * 100000 / duration);
                System.out.println("Took " + duration + "ms to query " + numElements + " vertices+edge 100 times. Rate = " + (duration / numElements) + "ms per vertex. TPS = " + tps);

                String calcStr = calcStrMap.get(numThreads);
                if (calcStr == null) {
                    calcStrMap.put(numThreads, "=(" + tps);
                } else {
                    calcStrMap.put(numThreads, calcStr + " + " + tps);
                }
            }
        }

        for (Map.Entry<Integer, String> entry : calcStrMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + ")/3");
        }

//        ((BitsyGraph)graph).setDefaultIsolationLevel(BitsyIsolationLevel.REPEATABLE_READ);
    }

    private void setException(Throwable t) {
        this.toThrow = t;
    }

    private Throwable getException() {
        return toThrow;
    }
}
