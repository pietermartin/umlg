##Quick Preview

A simple uml class diagram.

![image of person works for company](images/uml/Package_umlg_demoQuickPreviewClassDiagram.PNG)

The generated code.

    Company company = new Company();
    company.setName("Umlg");
    Person person = new Person();
    person.setFirstname("Joe");
    person.setLastname("Bloggs");
    company.getEmployee().add(person);
    GraphDb.getDb().commit();
    Assert.assertTrue(company.getEmployee().contains(person));

***



Getting Started
---------------

[Getting Started](http://www.umlg.org/gettingStarted.html) with maven and Papyrus.

The basic pattern used is that an entity wraps a vertex. Associations between entities are realized as edges between vertexes.

Umlg uses the [eclipse uml2 project](http://projects.eclipse.org/projects/modeling.mdt.uml2) to load the uml model into memory.
[Eclipse Papyrus](http://projects.eclipse.org/projects/modeling.mdt.papyrus) is the recommended uml2 modelling tool.
One of the primary objectives of Umlg is to implement the [UML2](http://www.omg.org/spec/UML/2.4.1/Superstructure/PDF)
semantics as accurately as possible.

Umlg can also, optionally, generate a rest interface for performing crud and query operations. A web interface is provided
to perform crud operations and execute queries. Queries can be executed in [OCL](http://www.omg.org/spec/OCL/2.3.1/PDF),
[Gremlin](https://github.com/tinkerpop/gremlin/wiki) or in the underlying graph databases' native query language.

Currently, [Bitsy](https://bitbucket.org/lambdazen/bitsy/wiki/Home), [OrientDb](http://www.orientdb.org/),
[Neo4j](http://www.neo4j.org/) and [Titan](https://github.com/thinkaurelius/titan/wiki) are supported as the underlying
blueprints graph databases.


To generate java entities add the following fragment to your pom.

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