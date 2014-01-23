##Quick Preview

A simple uml class diagram.

![image of person works for company](images/uml/Package_umlg_demoQuickPreviewClassDiagram.PNG)

Using the generated code.

    Company company = new Company();
    company.setName("Umlg");
    Person person = new Person();
    person.setFirstname("Joe");
    person.setLastname("Bloggs");
    company.getEmployee().add(person);
    GraphDb.getDb().commit();
    Assert.assertTrue(company.getEmployee().contains(person));

##Introduction

Umlg is a UML to java code generator. From class diagrams, persistent java entities are generated. The entities persist
via an embedded [Blueprints](http://blueprints.tinkerpop.com) graph databases. The semantics of a
[Property Graph Model](https://github.com/tinkerpop/blueprints/wiki/Property-Graph-Model) is a natural fit for
implementing the rich semantics of UML class diagrams in java.

The basic pattern used is that an entity wraps a vertex. Associations are realized as edges between vertexes.

Umlg entities are **not** POJOs. They are always persistent objects. They contain no annotations. Interceptors are not used in the
implementation. The only configuration required is one property specifying the location of the underlying graph database.
As such Umlg entities always execute, (unit test or production) in an equivalent environment.

Graph databases have very fast startup times that do not increase with the number of entities (vertices and edges).
Even on large projects with thousands of entities Umlg will still start up in milliseconds. No need for mock tests nor integration tests.

Graph databases are very good at traversing relationship. Its their speciality after all. This translates to Umlg entities
being very efficient and fast at navigating object oriented associations. (//TODO do a simple JPA comparison)

Umlg entities implement most class diagram constructs. This includes: inheritance, interfaces, abstract
classes, associations, composition, complex data types, multiplicity, collection types (Set, OrderedSet, Sequence, Bag),
qualifiers, constraints. One of the primary objectives of Umlg is to implement the [UML2](http://www.omg.org/spec/UML/2.4.1/Superstructure/PDF)
semantics as accurately as possible.

Umlg has full support for [OCL](href="http://www.omg.org/spec/OCL/2.3.1/PDF") (Object Constraint Language). OCL is a
powerful constraint and query language that operates directly on the entities. OCL constraints and queries can be specified
in the model at design time or executed runtime.

Umlg can also, optionally, generate a rest interface for performing crud and query operations. A web interface is provided
to perform crud operations and execute queries. Queries can be executed in [OCL](http://www.omg.org/spec/OCL/2.3.1/PDF),
[Gremlin](https://github.com/tinkerpop/gremlin/wiki) or in the underlying graph databases' native query language.

Currently, [Bitsy](https://bitbucket.org/lambdazen/bitsy/wiki/Home), [OrientDb](http://www.orientdb.org/),
[Neo4j](http://www.neo4j.org/) and [Titan](https://github.com/thinkaurelius/titan/wiki) are supported as the underlying
blueprints graph databases.

##Getting Started

***

For UML modeling [Eclipse Papyrus](http://projects.eclipse.org/projects/modeling.mdt.papyrus) is the recommended tool.

Download and install Papyrus.

***

Umlg uses [maven](http://maven.apache.org/) as its build tool.

The easiest way to get started is to generate a maven project using the Umlg Maven Archetype.

To generate a sample project, type in the following at your project's root directory.

    mvn archetype:generate -DarchetypeCatalog=local

You will be prompted to select the type of project you which to generate. Umlg can generate 3 types of sample projects.

* A minimalist project containing a sample uml model but no additional Umlg uml libraries.
* A sample uml model with Umlg's uml validation profile and data types library preloaded.
* A sample uml model with Umlg's uml validation profile and data types library preloaded.
    Includes Umlg's rest interface and gui.

For the purpose of this getting started guide, choose the **full** option.

The archetype will create a fully functional Umlg application. Go into the application and run

    maven install

Before compiling the application, maven will generate entities and a rest interface to the entities from the sample model.

To startup the rest server and use the web interface run

    mvn ant:run

and now go to [http://localhost:8111/demo-1/ui2](http://localhost:8111/demo-1/ui2) to view and use the application.

There is a sample junit test `org.umlg.test.TestDemo` in `src/test/java`. You can execute it to see Umlg in action.

###How it works

The archetype will generate a maven project with 2 sub modules.

* **generator** - This is responsible to load the uml model and generate the entities.
* **application** - This is where the generated entities and optional rest interface will go.

In the `generator` module there is only one java class `DemoGenerator`. Running this class's `main` method  will load
the sample model and generate the corresponding entities into the **application** module.

To generate code from the UML model the following maven dependency is required.

    <dependency>
        <groupId>org.umlg</groupId>
        <artifactId>java-generation</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>

and the code to generate the code.

    DemoGenerator demoGenerator = new DemoGenerator();
    demoGenerator.generate(
            new File("../application/src/main/model/umlg-demo1.uml"),
            new File("../application"),
            RestletVisitors.getDefaultJavaVisitors());

`DemoGenerator` takes 3 parameters. The first is the uml model, the second is the output location of the generated source
and the 3rd is the generation visitors. Umlg generates code via a sequential list of visitors to the UML model.
Each visitor implements some feature of the model in java.


The **application** module has a couple of source folders.

* **generated-java** - This is where the generated entities go.
* **generated-meta** - Each UML class has a corresponding singleton.
* **generated-restlet** - This is where the rest resources go.
* **resources** - A resources folder that contains `umlg.env.properties` It contains the property `umlg.db.location`. Change this to
 point the graph database to a location of your choosing.
* **test** In the test source folder `src/test/java` there is a sample junit test class `org.umlg.test.TestDemo`. You can execute
it to see Umlg in action.

To compile the entities and the following dependency is required. Replace the artifact with the underlying blueprints graph db of your choice.

    <dependency>
        <groupId>org.umlg</groupId>
        <artifactId>runtime-domain-bitsy</artifactId>
        <!--
        <artifactId>runtime-domain-orientdb</artifactId>
        <artifactId>runtime-domain-neo4j</artifactId>
        <artifactId>runtime-domain-titan</artifactId>
        -->
        <version>0.0.1-SNAPSHOT</version>
    </dependency>





Umlg uses the [Eclipse Uml2 project](http://projects.eclipse.org/projects/modeling.mdt.uml2) to load the uml model into
memory for code generation.

Umlg uses the [eclipse uml2 project](http://projects.eclipse.org/projects/modeling.mdt.uml2) to load the uml model into memory.
[Eclipse Papyrus](http://projects.eclipse.org/projects/modeling.mdt.papyrus) is the recommended uml2 modelling tool.


After the code is generated, to compile and use the entities add the following fragment to the pom. Replace the artifact
with the underlying blueprints graph db of your choice.

    <dependency>
        <groupId>org.umlg</groupId>
        <artifactId>runtime-domain-bitsy</artifactId>
        <!--
        <artifactId>runtime-domain-orientdb</artifactId>
        <artifactId>runtime-domain-neo4j</artifactId>
        <artifactId>runtime-domain-titan</artifactId>
        -->
        <version>0.0.1-SNAPSHOT</version>
    </dependency>

Umlg only needs one configuration property specified in `umlg.env.property` to specify where the data files should be
kept.

    umlg.db.location=/tmp/demo-quick-preview

Each class in the uml model have a respective java entity. To create a company and a person just instantiate `Company`
and `Person`.

    Company company = new Company();
    Person person1 = new Person();

To set the name properties just set them as normal.

    company1.setName("Coolraid");
    person1.setFirstName("Joe");
    person1.setLastName("Bloggs");

To add an employee to the company call an adder or just add it to the employee collection.

    company1.addToEmployee(person1);

or

    company1.getEmployee().add(person1);

and lastly to persist the entities call,

    GraphDb.getDb().commit()

`GraphDb.getDb()` returns an instance of `UmlgGraph`. `UmlgGraph` wraps the underlying blueprints graph. It is a
singleton and is always available on the current thread via `GraphDb.getDb()`



