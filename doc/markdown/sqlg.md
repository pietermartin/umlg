<!-- Sqlg -->

##Introduction

**Sqlg** is a implementation of [Tinkerpop3](https://github.com/tinkerpop/tinkerpop3) on a [RDBMS](http://en.wikipedia.org/wiki/Relational_database_management_system).
Currently [HSQLDB](http://hsqldb.org/) and [Postgresql](http://www.postgresql.org/) are supported.


###Tinkerpop supported features

Sqlg passes Tinkerpop's `StructureStandardSuite` and `ProcessStandardSuite` test suites.

Graph Features **not** implemented.

* Computer
* ThreadedTransactions
* Variables

Vertex Features **not** implemented.

* MultiProperties
* MetaProperties
* UserSuppliedIds

Edge Features **not** implemented.

* UserSuppliedIds

Vertex property features **not** implemented.

* MapValues
* MixedListValues
* SerializableValues
* UniformListValues

Edge property feature **not** implemented.

* MapValues
* MixedListValues
* SerializableValues
* UniformListValues

##Getting Started

<br />
###Maven
<br />

Maven coordinates,

**HSQLDB**

    <dependency>
        <groupId>org.umlg</groupId>
        <artifactId>sqlg-hsqldb</artifactId>
        <version>1.0.0.M1</version>
    </dependency>

**Postgresql**

    <dependency>
        <groupId>org.umlg</groupId>
        <artifactId>sqlg-postgres</artifactId>
        <version>1.0.0.M1</version>
    </dependency>

Sqlg is designed to run as a singleton that can be shared among multiple threads. Instantiate Sqlg via the standard
tinkerpop3 static constructors.

* `SqlgGraph.open(final Configuration configuration)`
* `SqlgGraph.open(final String pathToSqlgProperties)`

The configuration object requires the following properties.

**HSQLDB**

    jdbc.url=jdbc:hsqldb:file:/tmp/sqlg
    jdbc.username=SA
    jdbc.password=

**Postgresql**

    jdbc.url=jdbc:postgresql://localhost:5432/sqlgraphdb
    jdbc.username=postgres
    jdbc.password=******

In the case of Postgres the database must already exist.

If you want to run the Tinkerpop tests on Postgres you need to create the various databases used upfront.
These are,

* g1
* g2
* readGraph
* standard
* subgraph
* temp
* temp1
* temp2

<br />
###Gremlin Console
<br />

**HSQLDB**

    [pieter@pieter-laptop bin]$ ./gremlin.sh

             \,,,/
             (o o)
    -----oOOo-(3)-oOOo-----
    gremlin> :install org.umlg sqlg-hsqldb 1.0.0.M1
    ==>A module with the name sqlg-hsqldb is already installed
    gremlin> :plugin use tinkerpop.sqlg-hsqldb
    ==>tinkerpop.sqlg-hsqldb activated
    gremlin> g = SqlgGraph.open('/home/pieter/Downloads/sqlg/sqlg-hsqldb/src/test/resources/sqlg.properties')
    ==>sqlggraph[SqlGraph]
    gremlin> g.loadGraphML('../../data/grateful-dead.xml')
    ==>null
    gremlin> g.V().count()
    ==>2424
    gremlin>

**Postgresql**

    [pieter@pieter-laptop bin]$ ./gremlin.sh

             \,,,/
             (o o)
    -----oOOo-(3)-oOOo-----
    gremlin> :install org.umlg sqlg-postgres 0.0.2-SNAPSHOT
    ==>A module with the name sqlg-postgres is already installed
    gremlin> :plugin use tinkerpop.sqlg-postgres
    ==>tinkerpop.sqlg-postgres activated
    gremlin> g = SqlgGraph.open('/home/pieter/Downloads/sqlg/sqlg-postgres/src/test/resources/sqlg.properties')
    ==>sqlggraph[SqlGraph]
    gremlin> g.loadGraphML('../../data/grateful-dead.xml')
    ==>null
    gremlin> g.V().count()
    ==>2424
    gremlin>


<br />
##Data types
<br />
<div>
<table class="table table-striped table-bordered">
    <thead>
        <tr>
            <th>Java</th>
            <th>HSQLDB</th>
            <th>Postgres</th>
        </tr>
    </thead>
    <tbody>
        </tr>
            <td>Boolean</td>
            <td>BOOLEAN</td>
            <td>BOOLEAN</td>
        </tr>
        <tr>
            <td>Byte</td>
            <td>TINYINT</td>
            <td><strong>Not supported</strong></td>
        </tr>
        <tr>
            <td>Short</td>
            <td>SMALLINT</td>
            <td>SMALLINT</td>
        </tr>
        <tr>
            <td>Integer</td>
            <td>INTEGER</td>
            <td>INTEGER</td>
        </tr>
        <tr>
            <td>Long</td>
            <td>BIGINT</td>
            <td>BIGINTtd>
        </tr>
        <tr>
            <td>Float</td>
            <td><strong>Not supported</strong></td>
            <td>REAL</td>
        </tr>
        <tr>
            <td>Double</td>
            <td>DOUBLE</td>
            <td>DOUBLE PRECISION</td>
        </tr>
        <tr>
            <td>String</td>
            <td>LONGVARCHAR</td>
            <td>TEXT</td>
        </tr>
        <!-- Arrays -->
        <tr>
            <td>Boolean[]</td>
            <td>BOOLEAN ARRAY DEFAULT ARRAY[]</td>
            <td>BOOLEAN[]</td>
        </tr>
        <tr>
            <td>Byte[]</td>
            <td>LONGVARBINARY</td>
            <td>BYTEA</td>
        </tr>
        <tr>
            <td>Short[]</td>
            <td>SMALLINT ARRAY DEFAULT ARRAY[]</td>
            <td>SMALLINT[]</td>
        </tr>
        <tr>
            <td>Integer[]</td>
            <td>INTEGER ARRAY DEFAULT ARRAY[]</td>
            <td>INTEGER[]</td>
        </tr>
        <tr>
            <td>Long[]</td>
            <td>BIGINT ARRAY DEFAULT ARRAY[]</td>
            <td>BIGINT[]</td>
        </tr>
        <tr>
            <td>Float[]</td>
            <td>Not supported</td>
            <td>REAL[]</td>
        </tr>
        <tr>
            <td>Double[]</td>
            <td>DOUBLE ARRAY DEFAULT ARRAY[]</td>
            <td>DOUBLE PRECISION[]</td>
        </tr>
        <tr>
            <td>String[]</td>
            <td>LONGVARCHAR ARRAY DEFAULT ARRAY[]</td>
            <td>TEXT[]</td>
        </tr>
    </tbody>
</table>
</div>

<br />
##Architecture
<br />

With the coming of vertex label's to Tinkerpop3 the mapping of Tinkerpop's graph semantics to that of a RDBMS became natural and useful.

###Vertex table
Every unique vertex label maps to a table. Vertex tables are prefixed with a `V_`. i.e. `V_Person`. The vertex table
stores the vertex's properties.

###Edge table
Every unique edge label maps to a table. Edge tables are prefixed with a `E_`. i.e. `E_friend`. The edge table stores
the edge's adjacent vertex ids and the edge properties. The column corresponding to each adjacent vertex id (`IN` and `OUT`)
has a foreign key to the adjacent vertex's table.

###VERTICES and EDGES

There are two special tables in Sqlg. One for vertices (`VERTICES`) and one for edges (`EDGES`).

####VERTICES
The `VERTICES` table has one record for every vertex in the graph. The `VERTICES` tables' auto generated primary key
functions as the vertex id. Additionally the `VERTICES` table stores the vertex's label and the unique set of labels of
the vertex's incident edges.

Every vertex has a label and as such a vertex table. This table's `ID` column has a one to one mapping the `ID` column
of the `VERTICES` table.

This strategy allows gremlin queries of the form `g.V(1L)` to find the vertex in the `VERTICES` table and then know in which
table the vertex is stored.

Queries of the form `g.V().has(T.label, 'Person')` will go directly to the `V_Person` table to retrieve the vertices.

####EDGES
The `EDGES` table has one record for every edge in the graph. The `EDGES` tables' auto generated primary key
functions as the edge id. Additionally the `EDGES` table stores the edge's label.

Similar to the vertex look-ups the `EDGES` table facilitates implementing queries of the form `g.E(1L)`

###Tinkerpop-classic

![image of tinkerpop-classic](images/sqlg/tinkerpop-classic1-er.png)
![image of tinkerpop-classic](images/sqlg/tinkerpop-classic2-er.png)

####All tables
![image of tinkerpop-classic](images/sqlg/tinkerpop-classic.png)
####person####
![image of tinkerpop-classic](images/sqlg/person.png)
####software
![image of tinkerpop-classic](images/sqlg/software.png)
####knows
![image of tinkerpop-classic](images/sqlg/knows.png)
####created
![image of tinkerpop-classic](images/sqlg/created.png)
####VERTICES
![image of tinkerpop-classic](images/sqlg/vertices.png)
####EDGES
![image of tinkerpop-classic](images/sqlg/edges.png)

###Namespacing and Schemas

Many RDBMS databases have the notion of a `schema` as a namespace for tables. Sqlg supports schemas. Schemas
only apply to vertex labels. Edge tables are created in the schema of the adjacent `out` vertex.
By default all vertex tables go into the underlying databases' default schema. For Postgresql this
is the `public` schema.

To specify the schema for a label Sqlg uses the dot `.` notation.

    Vertex john = this.sqlgGraph.addVertex(T.label, "manager", "name", "john");
    Vertex palace1 = this.sqlgGraph.addVertex(T.label, "property.house", "name", "palace1");
    Vertex corrola = this.sqlgGraph.addVertex(T.label, "fleet.car", "model", "corrola");
    palace1.addEdge("managedBy", john);
    corrola.addEdge("owner", john);

This will create a table `V_manager` in the `public` (default) schema. A table `V_house` in a `property` schema and table `V_car`
in a `fleet` schema. For the edges a `E_managedBy` table is created in the `property` schema and a `E_owner` table in the `fleet` schema.

![image of tinkerpop-classic](images/sqlg/schemas.png)

<br />
##Indexes
<br />

Sqlg supports basic indexing.

`org.umlg.sqlg.structure.SqlgGraph` has two methods on it to create indexes. One for vertices and one for edges.

* `SqlgGraph.createVertexLabeledIndex(String label, Object... dummykeyValues)`
* `SqlgGraph.createEdgeLabeledIndex(String label, Object... dummykeyValues)`

The `dummykeyValues` are required to indicate to Sqlg the name and type of the property. The type is needed  for when
the column does not yet exist and Sqlg needs to create the relevant column.

Outside of creating the index Sqlg has no further direct interaction with index logic. However gremlin queries with a
`has` step will translate to a `sql` `where` clause. If an index had been created on the property of the `has` step then
the underlying sql engine will utilize the index on that field.

###Example

    @Test
    public void testIndexOnVertex() throws SQLException {
        this.sqlgGraph.createVertexLabeledIndex("Person", "name", "dummy");
        this.sqlgGraph.tx().commit();
        for (int i = 0; i < 5000; i++) {
            this.sqlgGraph.addVertex(T.label, "Person", "name", "john" + i);
        }
        this.sqlgGraph.tx().commit();
        assertEquals(1, this.sqlgGraph.V().has(T.label, "Person").has("name", "john50").count().next(), 0);

        //Check if the index is being used
        Connection conn = this.sqlgGraph.tx().getConnection();
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("explain analyze SELECT * FROM \"public\".\"V_Person\" a WHERE a.\"name\" = 'john50'");
        assertTrue(rs.next());
        String result = rs.getString(1);
        System.out.println(result);
        assertTrue(result.contains("Index Scan") || result.contains("Bitmap Heap Scan"));
        statement.close();
        this.sqlgGraph.tx().rollback();
    }

    Output: "Bitmap Heap Scan on "V_Person" a  (cost=4.42..32.42 rows=18 width=40) (actual time=0.016..0.016 rows=1 loops=1)"


In the above use case, Sqlg will create a table `V_Person` with column `name` together with a index on the `name`.
At present the default index is created. For postgresql this is a `Btree` index.

The gremlin query `this.sqlgGraph.V().has(T.label, "Person").has("name1", "john50")` will utilize the index on the `name` field.
Currently only `Compare.eq` is supported.

<br />
##Schema creation
<br />

Sqlg creates the schema lazily. This is great, however it comes with serious caveats.

**HSQLDB** does not support transactional schema creation. HSQLDB automatically commits any schema creation/alter command
and immediately starts a new transaction.
This can have some rather unfortunate consequences, as HSQLDB will silently commit a user transaction thus invalidating
the user's transaction semantics.

**Postgres** supports transactional schema creation/alter commands. The user's transaction semantics remains intact.
 However schema creation commands cause table level locks increasing the risk of dead locks in a multi threaded environment.

<br />
##Multiple Jvm
<br />


It is possible to run many Sqlg instances pointing to the same underlying database. These instances can be in the same jvm
but is primarily intended for separate jvm(s) pointing to the same underlying database.

Sqlg caches database schema information. When multiple Sqlg instances point to the same database,
Sqlg uses [Hazelcast](http://hazelcast.com/) as a distributed cache of the schema information.

To indicate to Sqlg that a `Hazelcast` cluster is required specify `hazelcast.members=ipaddres1,ipaddres2,ipaddres3`
in the constructors configuration object. Hazelcast will then automatically set up the distributed cluster for the schema
information.

<br />
##Batch mode
<br />

Postgres is significantly slower than HSQLDB. This is expected as Postgres runs as a server. HSQLDB shines when it runs
in embedded mode. (HSQLDB has not been tested in server mode!)

Sqlg supports a batch mode. This is currently only implemented on Postgres.
Batch mode is activated on the transaction object itself. After every `commit` batchMode needs to be reactivated.

    @Test
    public void testBatchMode() {
        this.sqlgGraph.tx().batchModeOn();
        for (int i = 0; i < 1000000; i++) {
            Vertex person1 = this.sqlgGraph.addVertex(T.label, "Person", "name", "a" + i);
            Vertex person2 = this.sqlgGraph.addVertex(T.label, "Person", "name", "b" + i);
            person1.addEdge("friend", person2, "context", 1);
            if (i != 0 && i % 100000 == 0) {
                this.sqlgGraph.tx().commit();
                this.sqlgGraph.tx().batchModeOn();
            }
        }
        this.sqlgGraph.tx().commit();
    }

With `batchMode` on Sqlg will cache all modification to the graph and on `commit` execute bulk sql statements.
This has a very significant improvement on performance.

<br />
##Performance Indicator
<br />

Below are some fairly trivial examples using Sqlg. The purpose is to give an indication of the performance that can be
expected by Sqlg.

All tests were run on a standard laptop.

* Intel(R) Core(TM) i7-4800MQ CPU @ 2.70GHz
* 500G Solid state drive

Running Tinkerpop's `StructurePerformanceTest` produces the following output

**HSQLDB**

    WriteToIO.writeGraphSON: [measured 10 out of 10 rounds, threads: 1 (sequential)]
     round: 1.42 [+- 1.82]...
    WriteToIO.writeKryo: [measured 10 out of 10 rounds, threads: 1 (sequential)]
     round: 0.70 [+- 0.11]...
    WriteToIO.writeGraphML: [measured 10 out of 10 rounds, threads: 1 (sequential)]
     round: 0.77 [+- 0.06]...
    WriteToGraph.writeEmptyVertices: [measured 10 out of 10 rounds, threads: 1 (sequential)]
     round: 3.31 [+- 0.43]...
    WriteToGraph.writeEmptyVerticesAndEdges: [measured 10 out of 10 rounds, threads: 1 (sequential)]
     round: 11.52 [+- 0.63]...
    ReadFromGraph.readAllProperties: [measured 10 out of 10 rounds, threads: 1 (sequential)]
     round: 4.26 [+- 0.97]...

**Postgres**

    WriteToIO.writeGraphSON: [measured 10 out of 10 rounds, threads: 1 (sequential)]
     round: 3.62 [+- 0.46]...
    WriteToIO.writeKryo: [measured 10 out of 10 rounds, threads: 1 (sequential)]
     round: 3.64 [+- 0.11]...
    WriteToIO.writeGraphML: [measured 10 out of 10 rounds, threads: 1 (sequential)]
     round: 4.24 [+- 0.10]...
    WriteToGraph.writeEmptyVertices: [measured 10 out of 10 rounds, threads: 1 (sequential)]
     round: 14.53 [+- 0.19]...
    WriteToGraph.writeEmptyVerticesAndEdges: [measured 10 out of 10 rounds, threads: 1 (sequential)]
     round: 48.64 [+- 0.41]...
    ReadFromGraph.readAllProperties: [measured 10 out of 10 rounds, threads: 1 (sequential)]
     round: 16.00 [+- 0.35]...

###Some trivial examples.

####Create a 10000 objects, each with 2 properties

    @Test
    public void testAddPersons() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < 10000; i++) {
            this.sqlgGraph.addVertex(T.label, "Person", "prop1", "property1", "prop2", "property2");
        }
        this.sqlgGraph.tx().commit();
        stopWatch.stop();
        System.out.println("Time to insert: " + stopWatch.toString());
        stopWatch.reset();
        stopWatch.start();
        Assert.assertEquals(Long.valueOf(10000), this.sqlgGraph.V().has(T.label, "Person").count().next());
        stopWatch.stop();
        System.out.println("Time to read: " + stopWatch.toString());
    }

**HSQLDB**

    Time to insert: 0:00:00.672
    Time to read: 0:00:00.124

**Postgres**

    Time to insert: 0:00:01.955
    Time to read: 0:00:00.117

Note, the Postgres read time is roughly equivalent to HSQLDB. This is because in the above test there is only one call to
the database. Postgres itself is fast, however round trips between client and server is expensive.

####Create a 10001 Persons, each with 2 properties and one friend

    @Test
    public void testAddPersonAndFriends() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Vertex previous = this.sqlgGraph.addVertex(T.label, "Person", "name", "first");

        for (int i = 0; i < 10000; i++) {
            Vertex current = this.sqlgGraph.addVertex(T.label, "Person", "name", "current" + i);
            previous.addEdge("friend", current);
        }
        this.sqlgGraph.tx().commit();
        stopWatch.stop();
        System.out.println("Time to insert: " + stopWatch.toString());
        stopWatch.reset();
        stopWatch.start();
        List<Vertex> persons = this.sqlgGraph.V().<Vertex>has(T.label, "Person").toList();
        Map<Vertex, List<Vertex>> friendMap = new HashMap<>();
        persons.forEach(
                p -> friendMap.put(p, p.in("friend").toList())
        );
        Assert.assertEquals(10001, friendMap.size());
        stopWatch.stop();
        System.out.println("Time to read all vertices: " + stopWatch.toString());
    }

**HSQLDB**

    Time to insert: 0:00:02.095
    Time to read all vertices: 0:00:01.138

**Postgres**

    Time to insert: 0:00:04.810
    Time to read all vertices: 0:00:09.177

To retrieve the friends a 1001 calls are made made. Postgres is significantly slower in this case.

####Postgres, Create a 1 000 000 Persons and Dogs with a pet edge. BatchMode on.

    @Test
    public void testPostgresBatchMode() {
        this.sqlgGraph.tx().batchModeOn();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 1; i < 1000001; i++) {
            Vertex person = this.sqlgGraph.addVertex(T.label, "Person", "name", "John" + i);
            Vertex dog = this.sqlgGraph.addVertex(T.label, "Dog", "name", "snowy" + i);
            person.addEdge("pet", dog);
            if (1 % 100000 == 0) {
                this.sqlgGraph.tx().commit();
                this.sqlgGraph.tx().batchModeOn();
            }
        }
        this.sqlgGraph.tx().commit();
        stopWatch.stop();
        System.out.println("Time to insert: " + stopWatch.toString());
        stopWatch.reset();
        stopWatch.start();

        Assert.assertEquals(1000000, this.sqlgGraph.V().<Vertex>has(T.label, "Person").count().next().intValue());
        Assert.assertEquals(1000000, this.sqlgGraph.V().<Vertex>has(T.label, "Dog").count().next().intValue());

        stopWatch.stop();
        System.out.println("Time to read all vertices: " + stopWatch.toString());
    }

**Postgres**

    Time to insert: 0:00:51.681
    Time to read all vertices: 0:00:16.130

####HSQLDB, Create a 1 000 000 Persons and Dogs with a pet edge.

    @Test
    public void testHsqldbLargeLoad() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 1; i < 1000001; i++) {
            Vertex person = this.sqlgGraph.addVertex(T.label, "Person", "name", "John" + i);
            Vertex dog = this.sqlgGraph.addVertex(T.label, "Dog", "name", "snowy" + i);
            person.addEdge("pet", dog);
            if (i % 100000 == 0) {
                this.sqlgGraph.tx().commit();
            }
        }
        this.sqlgGraph.tx().commit();
        stopWatch.stop();
        System.out.println("Time to insert: " + stopWatch.toString());
        stopWatch.reset();
        stopWatch.start();

        Assert.assertEquals(1000000, this.sqlgGraph.V().<Vertex>has(T.label, "Person").count().next().intValue());
        Assert.assertEquals(1000000, this.sqlgGraph.V().<Vertex>has(T.label, "Dog").count().next().intValue());

        stopWatch.stop();
        System.out.println("Time to read all vertices: " + stopWatch.toString());
    }

**HSQLDB**

    Time to insert: 0:02:32.435
    Time to read all vertices: 0:00:19.026

