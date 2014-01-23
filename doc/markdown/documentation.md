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

The entities are not POJOs. They are always persistent. They contain no annotations and no interceptors are used in the
implementation. The only configuration needed is one property to specify the location of the underlying graph database
on the file system. As such Umlg entities always execute, (unit test or production) in an equivalent environment. Graph
databases have very fast startup times that do not increase with the number of entities (vertices and edges).

Even on large projects with thousands of entities Umlg will still start up in milliseconds. No need for mock and integration tests.
No slow startup times while waiting for annotations to be parsed and databases to be created/updated.

Graph databases are very good at traversing relationship. Its their speciality after all. This translates to Umlg entities
being very efficient and performant at navigating object oriented associations. (//TODO do a simple JPA comparison)

Umlg entities implement most class diagram constructs. A non exhaustive list include, inheritance, interfaces, abstract
classes, associations, composition, complex data types, multiplicity, collection types (Set, OrderedSet, Sequence, Bag),
qualifiers, constraints.

Umlg has full support for [OCL](href="http://www.omg.org/spec/OCL/2.3.1/PDF") (Object Constraint Language). OCL is a
powerful constraint and query language that operates directly on the entities.

One of the primary objectives of Umlg is to implement the [UML2](http://www.omg.org/spec/UML/2.4.1/Superstructure/PDF)
semantics as accurately as possible.

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

1. A minimalist project containing a sample uml model but no additional Umlg uml libraries.
2. A sample uml model with Umlg's uml validation profile and data types library preloaded.
3. A sample uml model with Umlg's uml validation profile and data types library preloaded.
    Includes Umlg's rest interface and gui.

The archetype will generate a project with 2 modules.

1. The first is the generator. It is responsible to load the uml model and generate the entities.
2. The second is where the generated entities and optional rest interface will go.

After generating the project go to the `generator` module and execute `com.rorotika.DemoGenerator`'s main method. This
will load the sample model and generate the corresponding entities.

Now go to the `application` module. In the `generated-java` source folder you will find the generated entities
`org.umlg.One` and `org.umlg.Many`. The other classes will be explained later.

In `src/main/resources` you will find `umlg.env.properties`. It contains the property `umlg.db.location`. Change this to
point the graph database to a location of your choosing.

In the test source folder `src/test/java` there is a sample junit test class `org.umlg.test.TestDemo`. You can execute
it to see Umlg in action.



***

To load the model and generate java entities the following fragment is needed in your pom.

    <dependency>
        <groupId>org.umlg</groupId>
        <artifactId>java-generation</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>

The following code will generate the code. `JavaGenerator` takes 3 parameters. The first is the uml model, the
second is the output location of the generated source and the 3rd is the default generation visitors. Umlg generates
code via a sequential list of visitors to the Uml model. Each visitor implements some feature of the model in java.

    JavaGenerator javaGenerator = new JavaGenerator();
    javaGenerator.generate(
            new File("../application/src/main/model/umlg-demo1.uml"),
            new File("../application"), DefaultVisitors.getDefaultJavaVisitors());


***
***
***

Umlg uses the [Eclipse Uml2 project](http://projects.eclipse.org/projects/modeling.mdt.uml2) to load the uml model into
memory for code generation.

Umlg uses the [eclipse uml2 project](http://projects.eclipse.org/projects/modeling.mdt.uml2) to load the uml model into memory.
[Eclipse Papyrus](http://projects.eclipse.org/projects/modeling.mdt.papyrus) is the recommended uml2 modelling tool.


The following code will generate the code. `JavaGenerator` takes 3 parameters. The first is the uml model, the
second is the output location of the generated source and the 3rd is the default generation visitors. Umlg generates
code via a sequential list of visitors to the Uml model. Each visitor implements some feature of the model in java.

    JavaGenerator javaGenerator = new JavaGenerator();
    javaGenerator.generate(
            new File("../application/src/main/model/umlg-demo1.uml"),
            new File("../application"), DefaultVisitors.getDefaultJavaVisitors());

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



