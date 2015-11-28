<!-- Sqlg -->

##Introduction

**Sqlg** is a implementation of [TinkerPop3](https://github.com/tinkerpop/tinkerpop3) on a [RDBMS](http://en.wikipedia.org/wiki/Relational_database_management_system).
Currently [HSQLDB](http://hsqldb.org/) and [Postgresql](http://www.postgresql.org/) are supported.


Sqlg has a [Google Group](https://groups.google.com/forum/?hl=en#!forum/sqlg).


###TinkerPop supported features

Sqlg passes TinkerPop's `StructureStandardSuite` and `ProcessStandardSuite` test suites.

Graph Features **not** implemented.

* Computer
* ThreadedTransactions
* Variables

Vertex Features **not** implemented.

* MultiProperties
* MetaProperties
* UserSuppliedIds
* NumericIds
* StringIds
* UuidIds
* CustomIds
* AnyIds

Edge Features **not** implemented.

* UserSuppliedIds
* NumericIds
* StringIds
* UuidIds
* CustomIds
* AnyIds

Vertex property features **not** implemented.

* AddProperty
* RemoveProperty
* UserSuppliedIds
* NumericIds
* StringIds
* UuidIds
* CustomIds
* AnyIds
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
        <version>1.1.0-SNAPSHOT</version>
    </dependency>

**Postgresql**

    <dependency>
        <groupId>org.umlg</groupId>
        <artifactId>sqlg-postgres</artifactId>
        <version>1.1.0-SNAPSHOT</version>
    </dependency>

Sqlg is designed to run as a singleton that can be shared among multiple threads. You can instantiate Sqlg using the standard
tinkerpop3 static constructors.

* `SqlgGraph.open(final Configuration configuration)`
* `SqlgGraph.open(final String pathToSqlgProperties)`

The configuration object requires the following properties.

**HSQLDB**

    jdbc.url=jdbc:hsqldb:file:/tmp/sqlg
    jdbc.username=SA
    jdbc.password=

**Postgresql**

    jdbc.url=jdbc:postgresql://localhost:5432/yourdb
    jdbc.username=postgres
    jdbc.password=******

In the case of Postgres the database must already exist.

If you want to run the TinkerPop tests on Postgres you need to create upfront the various databases that are used.
These are,

* g1
* g2
* readGraph
* standard
* subgraph
* temp
* temp1
* temp2
* prototype

<br />
###Gremlin Console
<br />

**HSQLDB**

    [pieter@pieter-laptop bin]$ ./gremlin.sh
    
             \,,,/
             (o o)
    -----oOOo-(3)-oOOo-----
    plugin activated: tinkerpop.server
    plugin activated: tinkerpop.utilities
    plugin activated: tinkerpop.tinkergraph
    gremlin> :install org.umlg sqlg-hsqldb 1.1.0-SNAPSHOT
    log4j:WARN No appenders could be found for logger (org.apache.tinkerpop.gremlin.groovy.util.DependencyGrabber).
    log4j:WARN Please initialize the log4j system properly.
    log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.
    ==>Loaded: [org.umlg, sqlg-hsqldb, 1.1.0-SNAPSHOT]
    gremlin> :plugin list
    ==>tinkerpop.server[active]
    ==>tinkerpop.gephi
    ==>tinkerpop.utilities[active]
    ==>tinkerpop.sugar
    ==>tinkerpop.credentials
    ==>tinkerpop.tinkergraph[active]
    ==>sqlg.hsqldb
    gremlin> :plugin use sqlg.hsqldb
    ==>sqlg.hsqldb activated
    gremlin> graph = SqlgGraph.open('pathTo/sqlg.properties')
    ==>sqlggraph[SqlGraph]
    gremlin> g = graph.traversal()
    ==>graphtraversalsource[sqlggraph[SqlGraph], standard]
    gremlin> graph.io(graphml()).readGraph('../data/grateful-dead.xml')
    ==>null
    gremlin> g.V().count()
    ==>808
    gremlin> 

**Postgresql**

    
             \,,,/
             (o o)
    -----oOOo-(3)-oOOo-----
    plugin activated: tinkerpop.server
    plugin activated: tinkerpop.utilities
    plugin activated: tinkerpop.tinkergraph
    gremlin> :install org.umlg sqlg-postgres 1.1.0-SNAPSHOT
    log4j:WARN No appenders could be found for logger (org.apache.tinkerpop.gremlin.groovy.util.DependencyGrabber).
    log4j:WARN Please initialize the log4j system properly.
    log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.
    ==>Loaded: [org.umlg, sqlg-postgres, 1.1.0-SNAPSHOT]
    gremlin> :plugin list
    ==>tinkerpop.server[active]
    ==>tinkerpop.gephi
    ==>tinkerpop.utilities[active]
    ==>tinkerpop.sugar
    ==>tinkerpop.credentials
    ==>tinkerpop.tinkergraph[active]
    ==>sqlg.postgres
    gremlin> :plugin use sqlg.postgres
    ==>sqlg.postgres activated
    gremlin> graph = SqlgGraph.open('pathTo/sqlg.properties')
    ==>sqlggraph[SqlGraph]
    gremlin> g = graph.traversal()
    ==>graphtraversalsource[sqlggraph[SqlGraph], standard]
    gremlin> graph.io(graphml()).readGraph('../data/grateful-dead.xml')
    ==>null
    gremlin> g.V().count()
    ==>808
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
            <td>BIGINT</td>
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
        <tr>
            <td>java.time.LocalDateTime</td>
            <td>TIMESTAMP WITH TIME ZONE</td>
            <td>TIMESTAMP WITH TIME ZONE</td>
        </tr>
        <tr>
            <td>java.time.LocalDate</td>
            <td>DATE</td>
            <td>DATE</td>
        </tr>
        <tr>
            <td>java.time.LocalTime</td>
            <td>TIME WITH TIME ZONE</td>
            <td>TIME WITH TIME ZONE</td>
        </tr>
        <tr>
            <td>com.fasterxml.jackson.databind.JsonNode</td>
            <td><strong>Not supported</strong></td>
            <td>JSONB</td>
        </tr>
    </tbody>
</table>
</div>

**Note:** `java.time.LocalTime` drops the nano second precision.

<br />
##Architecture
<br />

With the coming of vertex labels to TinkerPop3 the mapping of TinkerPop's graph semantics to that of a RDBMS became natural and useful.

###Vertex tables
Every unique vertex label maps to a table. Vertex tables are prefixed with a `V_`. i.e. `V_Person`. The vertex table
stores the vertex's properties.

###Edge tables
Every unique edge label maps to a table. Edge tables are prefixed with a `E_`. i.e. `E_friend`. The edge table stores
each edge's adjacent vertex ids and the edge properties. The column corresponding to each adjacent vertex id (`IN` and `OUT`)
has a foreign key to the adjacent vertex's table.

From a rdbms' perspective each edge table is a classic `many to many` join table between vertices.

###TinkerPop-modern

Taken from [TinkerPop3](http://tinkerpop.incubator.apache.org/docs/3.0.0-SNAPSHOT/#intro)

![image of tinkerpop-classic](images/sqlg/tinkerpop-modern-graph.png)

####ER Diagram

![image of tinkerpop-classic](images/sqlg/tinkerpop-modern-er.png)

####V_person####
![image of tinkerpop-classic](images/sqlg/V_person.png)
####V_software
![image of tinkerpop-classic](images/sqlg/V_software.png)
####E_knows
![image of tinkerpop-classic](images/sqlg/E_knows.png)
####E_created
![image of tinkerpop-classic](images/sqlg/E_created.png)

###Namespacing and Schemas

Many RDBMS databases have the notion of a `schema` as a namespace for tables. Sqlg supports schemas
for vertex labels. Distinct schemas for edge tables are unnecessary as edge tables are created in the schema of the adjacent `out` vertex.
By default schemas for vertex tables go into the underlying databases' default schema. For postgresql and hsqldb this
is the `public` schema.

To specify the schema for a label Sqlg uses the dot `.` notation.

    Vertex john = this.sqlgGraph.addVertex(T.label, "manager", "name", "john");
    Vertex palace1 = this.sqlgGraph.addVertex(T.label, "continent.house", "name", "palace1");
    Vertex corrola = this.sqlgGraph.addVertex(T.label, "fleet.car", "model", "corrola");
    palace1.addEdge("managedBy", john);
    corrola.addEdge("owner", john);

This will create a table `V_manager` in the `public` (default) schema. Table `V_house` is in a `continent` schema and table `V_car`
is in a `fleet` schema. For the edges a `E_managedBy` table is created in the `continent` schema and a `E_owner` table in the `fleet` schema.

<br />
####Schemas
<br />
![image of tinkerpop-classic](images/sqlg/schemas.png)
![image of tinkerpop-classic](images/sqlg/continent.png)
![image of tinkerpop-classic](images/sqlg/fleet.png)
![image of tinkerpop-classic](images/sqlg/public.png)

<br />
##Indexes
<br />

Sqlg supports basic indexing.

`org.umlg.sqlg.structure.SqlgGraph` has two methods on it to create indexes one for vertices and one for edges.

* `SqlgGraph.createVertexLabeledIndex(String label, Object... dummykeyValues)`
* `SqlgGraph.createEdgeLabeledIndex(String label, Object... dummykeyValues)`

The `dummykeyValues` are required to indicate to Sqlg the name and type of the property. The type is needed when
the column does not yet exist and Sqlg needs to create it.

Outside of creating the index Sqlg has no further direct interaction with index logic. However gremlin queries with a
`has` step will translate to a sql `where` clause. If an index has been created on the property of the `has` step then
the underlying sql engine will utilize that index.

The index does not need to be created upfront. It can be added any time.

###Example

    @Test
    public void testIndexOnVertex() throws SQLException {
        this.sqlgGraph.createVertexLabeledIndex("Person", "name", "dummy");
        this.sqlgGraph.tx().commit();
        for (int i = 0; i < 5000; i++) {
            this.sqlgGraph.addVertex(T.label, "Person", "name", "john" + i);
        }
        this.sqlgGraph.tx().commit();
        assertEquals(1, this.sqlgGraph.traversal().V().has(T.label, "Person").has("name", "john50").count().next(), 0);

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

<br />
####Table definition
<br />
![image of tinkerpop-classic](images/sqlg/tableDefinition.png)

In the above example, Sqlg created a table `V_Person` with column `name` and an index on the `name` column.
Currently Sqlg only supports the default index. For postgresql this is a `Btree` index.


The output shows the result of a postgres query explain plan. The result shows that postgres does indeed utilize the index.

The gremlin query `this.sqlgGraph.traversal().V().has(T.label, "Person").has("name1", "john50")` will utilize the index on the `name` field.

<br />
##Schema creation
<br />

Sqlg creates the schema lazily. This is great, but comes with serious caveats.

**HSQLDB** does not support transactional schema creation. HSQLDB automatically commits any schema creation/alter command
and immediately starts a new transaction.
This can have some rather unfortunate consequences, as HSQLDB will silently commit a user transaction thus invalidating
the user's transaction boundaries and semantics.

**Postgres** supports transactional schema creation/alter commands. The user's transaction semantics remain intact.
 However schema creation commands creates table level locks which increases the risk of deadlocks in a multi-threaded environment. 
 Sqlg manages a global lock for schema creation to prevent postgres from dead locking.
 If multiple jvm(s) are used then a Hazelcast distributed lock is used.

<br />
##Multiple Jvm
<br />


It is possible to run many Sqlg instances pointing to the same underlying database. These instances can be in the same jvm
but is primarily intended for separate jvm(s) pointing to the same underlying database.

Sqlg caches database schema information. When multiple Sqlg instances point to the same database,
Sqlg uses [Hazelcast](http://hazelcast.com/) as a distributed cache of the schema information.

To indicate to Sqlg that a `Hazelcast` cluster is required  you must specify `hazelcast.members=ipaddres1,ipaddres2,ipaddres3`
in the constructors configuration object. Hazelcast will then automatically set up the distributed cluster for the schema
information.

<br />
####Example Postgresql
<br />

**Jvm 1**

    jdbc.url=jdbc:postgresql://localhost:5432/yourdb
    jdbc.username=postgres
    jdbc.password=******
    hazelcast.members=127.0.0.1,127.0.0.2
    
**Jvm 2**

    jdbc.url=jdbc:postgresql://localhost:5432/yourdb
    jdbc.username=postgres
    jdbc.password=******
    hazelcast.members=127.0.0.1,127.0.0.2

<br />
##Gremlin
<br />

Sqlg has full support for gremlin and passes the TinkerPop test suite. 
However gremlin's fine grained graphy nature result in very high latency. 

Sqlg optimizes gremlin by analyzing the steps and where possible combining them into a SqlgGraphStepCompiled or SqlgVertexStepCompiled.

Consecutive GraphStep, VertexStep, EdgeVertexStep, EdgeOtherVertexStep, HasStep, RepeatStep and OrderGlobalStep are currently combined.
The combined step will then in turn generate the sql statements to retrieve the data. It attempts to retrieve the data in as few distinct sql statements as possible.

**Note:** Turn sql logging on by setting `log4j.logger.org.umlg.sqlg=debug`

<br />
####Example
<br />

    @Test
    public void showHighLatency() {
        Vertex easternUnion = this.sqlgGraph.addVertex(T.label, "Organization", "name", "EasternUnion");
        Vertex legal = this.sqlgGraph.addVertex(T.label, "Division", "name", "Legal");
        Vertex dispatch = this.sqlgGraph.addVertex(T.label, "Division", "name", "Dispatch");
        Vertex newYork = this.sqlgGraph.addVertex(T.label, "Office", "name", "NewYork");
        Vertex singapore = this.sqlgGraph.addVertex(T.label, "Office", "name", "Singapore");
        easternUnion.addEdge("organization_division", legal);
        easternUnion.addEdge("organization_division", dispatch);
        legal.addEdge("division_office", newYork);
        dispatch.addEdge("division_office", singapore);
        this.sqlgGraph.tx().commit();
        
        GraphTraversal<Vertex, Vertex> traversal = this.sqlgGraph.traversal().V()
            .hasLabel("Organization")
            .out()
            .out();
        System.out.println(traversal);
        traversal.hasNext();
        System.out.println(traversal);
        List<Vertex> offices = traversal.toList();
        assertEquals(2, offices.size());
    }
    
    Before optimization:
    [GraphStep([],vertex), HasStep([~label.eq(Organization)]), VertexStep(OUT,vertex), VertexStep(OUT,vertex)]
    After optimization:
    [SqlgGraphStepCompiled([],vertex)]
    
Without optimization the query `this.sqlgGraph.traversal().V().hasLabel("Organization").out().out()` will result
in a number of database hits. First to get the organizations, then for each organization the divisions and then for each division the offices.
For an embedded db like HSQLDB this is still ok but for a database server like postgresql the performance impact is significant.

In the above example the GraphStep, HasStep and 2 VertexSteps are all combined into one step, SqlgGraphStepCompiled.

The above example will retrieve the data in one sql query.

    SELECT
	    "public"."V_Office"."ID" AS "alias1",
    	"public"."V_Office"."name" AS "alias2"
    FROM
    	"public"."V_Organization" INNER JOIN
    	"public"."E_organization_division" ON "public"."V_Organization"."ID" = "public"."E_organization_division"."public.Organization__O" INNER JOIN
    	"public"."V_Division" ON "public"."E_organization_division"."public.Division__I" = "public"."V_Division"."ID" INNER JOIN
    	"public"."E_division_office" ON "public"."V_Division"."ID" = "public"."E_division_office"."public.Division__O" INNER JOIN
    	"public"."V_Office" ON "public"."E_division_office"."public.Office__I" = "public"."V_Office"."ID"

<br />
###Predicates 
<br />

TinkerPop's [Compare](http://tinkerpop.apache.org/javadocs/3.1.0-incubating/core/org/apache/tinkerpop/gremlin/process/traversal/Compare.html) and 
[Contains](http://tinkerpop.apache.org/javadocs/3.1.0-incubating/core/org/apache/tinkerpop/gremlin/process/traversal/Contains.html) predicates are optimized 
to execute on the database.

<br />
####Compare example
<br />

    @Test
    public void showComparePredicate() {
        Vertex easternUnion = this.sqlgGraph.addVertex(T.label, "Organization", "name", "EasternUnion");
        Vertex legal = this.sqlgGraph.addVertex(T.label, "Division", "name", "Legal");
        Vertex dispatch = this.sqlgGraph.addVertex(T.label, "Division", "name", "Dispatch");
        Vertex newYork = this.sqlgGraph.addVertex(T.label, "Office", "name", "NewYork");
        Vertex singapore = this.sqlgGraph.addVertex(T.label, "Office", "name", "Singapore");
        easternUnion.addEdge("organization_division", legal);
        easternUnion.addEdge("organization_division", dispatch);
        legal.addEdge("division_office", newYork);
        dispatch.addEdge("division_office", singapore);
        this.sqlgGraph.tx().commit();

        GraphTraversal<Vertex, Vertex> traversal = this.sqlgGraph.traversal().V()
            .hasLabel("Organization")
            .out()
            .out()
            .has("name", P.eq("Singapore"));
        System.out.println(traversal);
        traversal.hasNext();
        System.out.println(traversal);
        List<Vertex> offices = traversal.toList();
        assertEquals(1, offices.size());
        assertEquals(singapore, offices.get(0));
    }
    
And the resulting sql,    
    
    SELECT
    	"public"."V_Office"."ID" AS "alias1",
    	"public"."V_Office"."name" AS "alias2"
    FROM
    	"public"."V_Organization" INNER JOIN
    	"public"."E_organization_division" ON "public"."V_Organization"."ID" = "public"."E_organization_division"."public.Organization__O" INNER JOIN
    	"public"."V_Division" ON "public"."E_organization_division"."public.Division__I" = "public"."V_Division"."ID" INNER JOIN
    	"public"."E_division_office" ON "public"."V_Division"."ID" = "public"."E_division_office"."public.Division__O" INNER JOIN
    	"public"."V_Office" ON "public"."E_division_office"."public.Office__I" = "public"."V_Office"."ID"
    WHERE
    	( "public"."V_Office"."name" = ?) 
    	
The same pattern is used for all the  [Compare](http://tinkerpop.apache.org/javadocs/3.1.0-incubating/core/org/apache/tinkerpop/gremlin/process/traversal/Compare.html) predicates.   	

<br />
####Contains example
<br />

Sqlg's implementation of [Contains](http://tinkerpop.apache.org/javadocs/3.1.0-incubating/core/org/apache/tinkerpop/gremlin/process/traversal/Contains.html) is slightly more complex.
For HSQLDB a regular `in` clause is used.

For postgresql, instead of using a sql `in` clause, i.e. `where property in (?, ?...)` the values are bulk inserted into a temporary table and then a join to the temporary table is used
to constrain the results.

    @Test
    public void showContainsPredicate() {
        List<Integer> numbers = new ArrayList<>(10000);
        for (int i = 0; i < 10000; i++) {
            this.sqlgGraph.addVertex(T.label, "Person", "number", i);
            numbers.add(i);
        }
        this.sqlgGraph.tx().commit();

        List<Vertex> persons = this.sqlgGraph.traversal().V()
                .hasLabel("Person")
                .has("number", P.within(numbers))
                .toList();
                
        assertEquals(10000, persons.size());
    }

And the resulting sql on postgresql,    

    CREATE TEMPORARY TABLE "V_BULK_TEMP_EDGEzf++PItI"("ID" SERIAL PRIMARY KEY, "within" INTEGER) ON COMMIT DROP;
    COPY "V_BULK_TEMP_EDGEzf++PItI" ("within") FROM stdin DELIMITER '	';
    SELECT
	    "public"."V_Person"."ID" AS "alias1",
    	"public"."V_Person"."number" AS "alias2"
    FROM
    	"public"."V_Person"
    INNER JOIN  "V_BULK_TEMP_EDGEzf++PItI" tmp1 on"public"."V_Person"."number" = tmp1.within
    
This pattern makes `P.within` and `p.without` very fast even with millions of values being passed into the query. 
Benchmarking shows that doing a join on a temporary table is always faster than using the `in` clause. 
For the case of there being only one value Sqlg will use an `equals` instead of a temporay table or an `in` statement.

<br />
####Text predicate 
<br />

Sqlg includes its own Text predicate for full text queries.

* Text.contains (case sensitive string contains)
* Text.ncontains (case sensitive string does not contain)
* Text.containsCIS (case insensitive string contains)
* Text.ncontainsCIS (case insensitive string does not contain)
* Text.startsWith (case sensitive string starts with)
* Text.nstartsWith (case sensitive string does not start with)
* Text.endsWith (case sensitive string ends with)
* Text.nendsWith (case sensitive string does not end with)

<br />

    @Test
    public void showTextPredicate() {
        Vertex john = this.sqlgGraph.addVertex(T.label, "Person", "name", "John XXX Doe");
        Vertex peter = this.sqlgGraph.addVertex(T.label, "Person", "name", "Peter YYY Snow");
        this.sqlgGraph.tx().commit();

        List<Vertex> persons = this.sqlgGraph.traversal().V()
                .hasLabel("Person")
                .has("name", Text.contains("XXX")).toList();
        
        assertEquals(1, persons.size());
        assertEquals(john, persons.get(0));
    }

And the resulting sql on postgresql,    

    SELECT
    	"public"."V_Person"."ID" AS "alias1",
	    "public"."V_Person"."name" AS "alias2"
    FROM
	    "public"."V_Person"
    WHERE
	    ( "public"."V_Person"."name" like ?)
	    

<br />
####LocalDateTime, LocalDate and LocalTime queries
<br />

LocalDateTime, LocalDate and LocalTime queries are supported.

    @Test
    public void showSearchOnLocalDateTime() {
        LocalDateTime born1 = LocalDateTime.of(1990, 1, 1, 1, 1, 1);
        LocalDateTime born2 = LocalDateTime.of(1990, 1, 1, 1, 1, 2);
        LocalDateTime born3 = LocalDateTime.of(1990, 1, 1, 1, 1, 3);
        Vertex john = this.sqlgGraph.addVertex(T.label, "Person", "name", "John", "born", born1);
        Vertex peter = this.sqlgGraph.addVertex(T.label, "Person", "name", "Peter", "born", born2);
        Vertex paul = this.sqlgGraph.addVertex(T.label, "Person", "name", "Paul", "born", born3);
        this.sqlgGraph.tx().commit();

        List<Vertex> persons = this.sqlgGraph.traversal().V().hasLabel("Person")
                .has("born", P.eq(born1))
                .toList();
        assertEquals(1, persons.size());
        assertEquals(john, persons.get(0));

        persons = this.sqlgGraph.traversal().V().hasLabel("Person")
                .has("born", P.between(LocalDateTime.of(1990, 1, 1, 1, 1, 1), LocalDateTime.of(1990, 1, 1, 1, 1, 3)))
                .toList();
        //P.between is inclusive to exclusive
        assertEquals(2, persons.size());
        assertTrue(persons.contains(john));
        assertTrue(persons.contains(peter));
    }
    
And the resulting sql,    

    SELECT
    	"public"."V_Person"."ID" AS "alias1",
    	"public"."V_Person"."born" AS "alias2",
    	"public"."V_Person"."name" AS "alias3"
    FROM
    	"public"."V_Person"
    WHERE
    	( "public"."V_Person"."born" = ?)
	
    SELECT
    	"public"."V_Person"."ID" AS "alias1",
    	"public"."V_Person"."born" AS "alias2",
    	"public"."V_Person"."name" AS "alias3"
    FROM
    	"public"."V_Person"
    WHERE
    	( "public"."V_Person"."born" >= ?) AND ( "public"."V_Person"."born" < ?)	
    
    
<br />
###Order
<br />

Sqlg optimizes the OrderGlobalStep if the data that the order applies to can be retrieved in one sql statement.
If not then order the ordering occurs in java via the OrderGlobalStep as per normal.

    @Test
    public void testOrderBy() {
        Vertex a1 = this.sqlgGraph.addVertex(T.label, "A", "name", "a", "surname", "a");
        Vertex a2 = this.sqlgGraph.addVertex(T.label, "A", "name", "a", "surname", "b");
        Vertex a3 = this.sqlgGraph.addVertex(T.label, "A", "name", "a", "surname", "c");
        Vertex b1 = this.sqlgGraph.addVertex(T.label, "A", "name", "b", "surname", "a");
        Vertex b2 = this.sqlgGraph.addVertex(T.label, "A", "name", "b", "surname", "b");
        Vertex b3 = this.sqlgGraph.addVertex(T.label, "A", "name", "b", "surname", "c");
        this.sqlgGraph.tx().commit();

        List<Vertex> result = this.sqlgGraph.traversal().V().hasLabel("A")
                .order().by("name", Order.incr).by("surname", Order.decr)
                .toList();

        assertEquals(6, result.size());
        assertEquals(a3, result.get(0));
        assertEquals(a2, result.get(1));
        assertEquals(a1, result.get(2));
        assertEquals(b3, result.get(3));
        assertEquals(b2, result.get(4));
        assertEquals(b1, result.get(5));
    }


And the resulting sql,    

    SELECT
	    "public"."V_A"."ID" AS "alias1",
    	"public"."V_A"."surname" AS "alias2",
    	"public"."V_A"."name" AS "alias3"
    FROM
    	"public"."V_A"
    ORDER BY
    	 "alias3" ASC,
    	 "alias2" DESC
    	 
<br />
###RepeatStep
<br />

Sqlg optimizes the RepeatStep so long as the `until` modulator is not present. 
`RepeatStep` can be optimized with the modulator `emit` and `times`.

    @Test
    public void showRepeat() {
        Vertex john = this.sqlgGraph.addVertex(T.label, "Person", "name", "John");
        Vertex peterski = this.sqlgGraph.addVertex(T.label, "Person", "name", "Peterski");
        Vertex paul = this.sqlgGraph.addVertex(T.label, "Person", "name", "Paul");
        Vertex usa = this.sqlgGraph.addVertex(T.label, "Country", "name", "USA");
        Vertex russia = this.sqlgGraph.addVertex(T.label, "Country", "name", "Russia");
        Vertex washington = this.sqlgGraph.addVertex(T.label, "City", "name", "Washington");
        john.addEdge("lives", usa);
        peterski.addEdge("lives", russia);
        usa.addEdge("capital", washington);
        this.sqlgGraph.tx().commit();

        Tree tree = this.sqlgGraph.traversal().V().hasLabel("Person").emit().times(2).repeat(__.out("lives", "capital")).tree().next();
        System.out.println(tree);
    }
    
    Output:
    
    [John]
    [John, USA]
    [John, USA, Washington]
    [Peterski]
    [Peterski, Russia]
    [Paul]
    
And the resulting sql,    

    SELECT
	    "public"."V_City"."ID" AS "alias1",
    	"public"."V_City"."name" AS "alias2",
    	"public"."V_Person"."ID" AS "alias3",
    	"public"."V_Person"."name" AS "alias4",
    	"public"."V_Country"."ID" AS "alias5",
    	"public"."V_Country"."name" AS "alias6",
    	"public"."V_City"."ID" AS "alias7",
    	"public"."V_City"."name" AS "alias8",
    	"public"."E_lives"."ID" AS "alias9"
    FROM
	    "public"."V_Person" LEFT JOIN
    	"public"."E_lives" ON "public"."V_Person"."ID" = "public"."E_lives"."public.Person__O" LEFT JOIN
    	"public"."V_Country" ON "public"."E_lives"."public.Country__I" = "public"."V_Country"."ID" LEFT JOIN
	    "public"."E_capital" ON "public"."V_Country"."ID" = "public"."E_capital"."public.Country__O" LEFT JOIN
    	"public"."V_City" ON "public"."E_capital"."public.City__I" = "public"."V_City"."ID"


This is an optimized way to retrieve whole sub-graphs with one hit to the db.

**Note:** The generated sql uses a `left join` if the repeat statements has an `emit` modulator.

<br />
##Batch mode
<br />

Postgres is significantly slower than HSQLDB. This is expected as Postgres runs as a server and suffers from latency. 
HSQLDB shines in embedded mode. (HSQLDB has not been tested in server mode!)

Sqlg supports various batch modes. Batch modes are only implemented on Postgresql.
Batch mode is activated on the transaction object itself. After every `commit` batchMode needs to be reactivated.

####Example Postgresql

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

With `batchMode` on Sqlg will cache all modifications to the graph and on `commit` execute bulk sql statements.
This has a significant improvement of performance.

<br />
##Performance Indicator
<br />

Below are some fairly trivial examples using Sqlg, the purpose of which is to give an indication of the performance that can be
expected from Sqlg.

All tests were run on a standard laptop with the following specs.

* Intel(R) Core(TM) i7-4800MQ CPU @ 2.70GHz
* 500G Solid state drive

Running TinkerPop's `StructurePerformanceTest` produces the following output

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

####Create 10000 objects, each with 2 properties

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

Note that the Postgres read time is roughly equivalent to that of HSQLDB. This is because in the above test there is only one call to
the database. Postgres itself is fast, however round trips between client and server are expensive.

####Create 10001 Persons, each with 2 properties and one friend

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

To retrieve the friends 1001 calls are made made. Postgres is significantly slower in this case.

####Postgres, Create 1 000 000 Persons and Dogs with a pet edge. BatchMode on.

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

####HSQLDB, Create 1 000 000 Persons and Dogs with a pet edge.

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

