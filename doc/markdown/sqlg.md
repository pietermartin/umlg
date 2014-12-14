<!-- Sqlg -->

##Introduction

**Sqlg** is a implementation of [Tinkerpop3](https://github.com/tinkerpop/tinkerpop3) on a [RDBMS](http://en.wikipedia.org/wiki/Relational_database_management_system).
Currently [HSQLDB](http://hsqldb.org/) and [Postgresql](http://www.postgresql.org/) is supported.

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
        <version>0.0.2-SNAPSHOT</version>
    </dependency>

**Postgresql**

    <dependency>
        <groupId>org.umlg</groupId>
        <artifactId>sqlg-postgres</artifactId>
        <version>0.0.2-SNAPSHOT</version>
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

<br />
###Gremlin Console
<br />

**HSQLDB**

    [pieter@pieter-laptop bin]$ ./gremlin.sh

             \,,,/
             (o o)
    -----oOOo-(3)-oOOo-----
    gremlin> :install org.umlg sqlg-hsqldb 0.0.2-SNAPSHOT
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
##Indexes
<br />

Sqlg supports basic indexing.

`org.umlg.sqlg.structure.SqlgGraph` has two methods on it to create indexes. One for vertices and one for edges.

* `SqlgGraph.createVertexLabeledIndex(String label, Object... dummykeyValues)`
* `SqlgGraph.createEdgeLabeledIndex(String label, Object... dummykeyValues)`


The `dummykeyValues` are required to indicate to Sqlg the name and type. The type is needed in case Sqlg needs
to create the relevant column.

###Example

    @Test
    public void testIndexOnVertex() {
        this.sqlgGraph.createVertexLabeledIndex("Person", "name", "dummy");
        this.sqlgGraph.tx().commit();
        for (int i = 0; i < 5000; i++) {
            this.sqlgGraph.addVertex(T.label, "Person", "name", "john" + i);
        }
        this.sqlgGraph.tx().commit();
        Assert.assertEquals(1, this.sqlgGraph.V().has(T.label, "Person").has("name1", Compare.eq, "john50").count().next().intValue());
    }

The gremlin query `this.sqlgGraph.V().has(T.label, "Person").has("name1", Compare.eq, "john50")` will utilize the index on the `name` field.
Currently only `Compare.eq` is supported.

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