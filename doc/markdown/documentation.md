#Documentation

Umlg documentation consist of,

1. A quick preview,
2. a short introduction
3. a getting started guide
4. a list of popular graphs modeled in UML and demonstrated in Umlg
5. a reference of UML features and its realization as Umlg entities
6. an Ocl reference
7. Umlg's gremlin add-on

##Quick Preview

A simple uml class diagram,

![image of person works for company](images/uml/Package_umlg_demoQuickPreviewClassDiagram.PNG)

and the generated entities in action.

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

One of the primary objectives of Umlg is to implement the [UML2](http://www.omg.org/spec/UML/2.4.1/Superstructure/PDF)
semantics as accurately as possible.

UML has strong and detailed semantics for specifying structural features. In general it is far easier and quicker to
specify the complex structure and relationships of domain entities in UML. Umlg makes that specification much more than
just documentation. A large part of the lack of popularity of UML is, that it remains only documentation. Many UML tools
can generate java, however it is seldom if ever generates persistent entities.

Umlg entities are **not** POJOs. They are always persistent objects. Interceptors are not used in the implementation and
they contain no annotations. The only configuration required is one property specifying the location of the underlying
graph database. As such Umlg entities always execute, (unit test or production) in an equivalent environment. The basic
pattern used is that, an entity wraps a vertex and associations are realized as edges between vertexes.

Most graph dbs supports a memory only db. Enabling that the entities will reside in memory only.

Graph databases have very fast startup times that do not increase with the number of entities (vertices and edges).
Even on large projects with thousands of entities Umlg will still start up in milliseconds. No need for mock tests nor
integration tests.

Graph databases are very good at traversing relationship. Its their speciality after all. This translates to Umlg entities
being very efficient and fast at navigating object oriented associations. (//TODO do a simple JPA comparison)

Umlg entities implement most class diagram constructs. This includes: inheritance, interfaces, abstract
classes, associations, composition, complex data types, multiplicity, collection types (Set, OrderedSet, Sequence, Bag),
qualifiers, constraints.

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

To startup the rest server and use the web interface `cd` to the **application** module and run

    mvn exec:exec

and now go to [http://localhost:8111/demo/ui2](http://localhost:8111/demo/ui2) to view and use the application.

Lastly there is a sample junit test `org.umlg.test.TestDemo` in `src/test/java`. You can execute it to see Umlg in action.

###How the build works

The archetype will generate a maven project with 2 sub modules. The 2 modules are,

* **generator** - This is responsible to load the uml model and generate the entities.
* **application** - This is where the generated entities and optional rest interface will go.

<br />
####generator module
<br />

In the `generator` module there is only one java class `DemoGenerator`. Running this class' `main` method  will load
the sample model and generate the corresponding entities into the **application** module.

For `DemoGenerator` to compile and be able to generate code the following maven dependency is required.

    <dependency>
        <groupId>org.umlg</groupId>
        <artifactId>java-generation</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>

and the code to generate the code.

    public class DemoGenerator {
        public static void main(String[] args) throws URISyntaxException {
            if (args.length == 0) {
                args = new String[]{"."};
            }
            JavaGenerator javaGenerator = new JavaGenerator();
            javaGenerator.generate(
                    new File(args[0] + "/application/src/main/model/umlg-demo1.uml"),
                    new File(args[0] + "/application"), RestletVisitors.getDefaultJavaVisitors());

        }
    }

`org.umlg.generation.JavaGenerator` is the entry point to generating code in Umlg from UML. `JavaGenerator.main` takes 3 parameters.
The first is the uml model, the second is the output location of the generated source and the 3rd is the generation visitors.
Umlg generates code via a sequential list of visitors to the UML model. Each visitor implements some feature of the model in java.


It is possible to add custom visitors to customize the entities. This is explained [here //TODO](http://localhost#todo).

<br />
####application module
<br />

The **application** module has 3 java source folders.

* **generated-java** - This is where the generated entities go.
* **generated-java-meta** - Each UML class has a corresponding singleton.
* **generated-java-restlet** - This is where the rest resources go.

And a resources folder,

* **resources** - A resources folder that contains `umlg.env.properties`.<br />
It contains the property, `umlg.db.location=/tmp/demo-quick-preview`<br />
Change this to point the graph database to a location of your choosing.

And a test java source folder

* **test** This contains a sample junit test class `org.umlg.test.TestDemo`. Run it to see Umlg in action.

<br />

To compile the entities the following dependency is required. Replace the artifact with the underlying blueprints graph db of your choice.
This dependency will bring in the underlying graph db and everything the Umlg entities need.

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

<br />

To compile and execute the rest resources the following dependency is required
This brings in [Restlet](http://restlet.org/). Restlet is the rest framework used by Umlg to expose the entities as rest resources.

         <dependency>
             <groupId>org.umlg</groupId>
             <artifactId>runtime-restlet</artifactId>
             <version>0.0.1-SNAPSHOT</version>
         </dependency>

<br />
And lastly to use the web ui add the following dependency This makes available web resources. The javascript, html and
css files needed for the Umlg admin gui.

         <dependency>
             <groupId>org.umlg</groupId>
             <artifactId>runtime-ui</artifactId>
             <version>0.0.1-SNAPSHOT</version>
         </dependency>

<br />
###The entities
<br />

The generated demo's model models a simple One to Many relationship.

![One to Many](images/uml/Package_umlg_demo1ClassDiagram.PNG)

Each class in the uml model have a respective java entity. To create a company and a person just instantiate `Company`
and `Person`.

    One one = new One();
    Many many = new Many();

For each property a standard setter and getter is generated.

    one.setName("Coolraid");
    many.setName("Joe");
    one.setMany(many);


For each property with a multiplicity greater than 1 an adder method is generated. This is different to the setter in
that it appends to the collection. The setter replaces the collection.

    one.addToMany(many);

or just use the standard java collection 'add' method

    one.getMany().add(many);

Similarly a remover is generated

    one.removeFromMany(many);

or use the standard java collection 'remove' method

    one.getMany().remove(many);

and lastly to persist the entities call,

    GraphDb.getDb().commit()

or rollback the transaction

    GraphDb.getDb().rollback()

`GraphDb.getDb()` returns an instance of `UmlgGraph`. `UmlgGraph` wraps the underlying blueprints graph. It is a
singleton and is always available on the current thread via `GraphDb.getDb()`



