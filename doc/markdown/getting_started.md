<!-- Getting started guide -->

#Getting Started

***

For UML modeling [Eclipse Papyrus](http://projects.eclipse.org/projects/modeling.mdt.papyrus) is the recommended tool.

So you will need to download and install Papyrus.

***

UMLG uses [maven](http://maven.apache.org/) as its build tool.

The easiest way to get started is to generate a maven project using the UMLG Maven Archetype.

To generate a sample project, type in the following at your project's root directory.

    mvn archetype:generate -DarchetypeCatalog=local

You will be prompted to select the project you which to generate. Choose `org.umlg:umlg-archetype-full`

Follow the prompts to select your maven groupId, artifactId, version, java package and underlying graph database.
A sample uml model with UMLG's uml validation profile and
data types library will be preloaded. The archetype will create a fully functional UMLG application.

`cd artifactId` into the application you just generated and run `maven install`

Before compiling the application, maven will generate the entities and a rest interface to the entities from the sample model.

For the impatient to start the rest server and use the web interface `cd artifactId-application/artifactId-jetty` and run `mvn exec:exec`
You can view and use the application at [http://localhost:8080/demo/ui2](http://localhost:8111/demo/ui2).

Lastly there is a sample junit test `org.umlg.test.TestDemo` in `src/test/java`. You can execute it to see UMLG in action.

##How the build works

The archetype will generate a maven project with 2 sub modules. The 2 modules are,

* **generator** - This is responsible to load the uml model and generate the entities.
* **application** - This is where the generated entities and optional rest interface will go.

<br />
###generator module
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
                    new File(args[0] + "/application"),
                    RestletVisitors.getDefaultJavaVisitors());

        }
    }

`org.umlg.generation.JavaGenerator` is the entry point to generating code in UMLG from UML. `JavaGenerator.main` takes 3 parameters.
The first is the uml model, the second is the output location of the generated source and the 3rd is the generation visitors.
UMLG generates code via a sequential list of visitors to the UML model. Each visitor implements some feature of the model in java.


It is possible to add custom visitors to customize the entities. This is explained [here //TODO](http://localhost/todo).

<br />
###application module

The **application** module has 4 sub modules.

####entities

This is where the generated entities are. It also contains a resources folder with `umlg.env.properties`.<br />
It contains the commented out property `umlg.db.location=/tmp`. UMLG defaults to the systems tmp directory. Change this
property to point the db to a location of your choice.<br />

A sample junit test class `org.umlg.test.TestDemo` is provided. Run it to see UMLG in action.

To compile the entities the following dependency is required. Replace the artifact with the underlying blueprints graph db of your choice.
This dependency will bring in the underlying graph db and everything the UMLG entities need.

    <dependency>
        <groupId>org.umlg</groupId>
        <artifactId>runtime-domain-bitsy</artifactId>
        <!--
        <artifactId>runtime-domain-neo4j</artifactId>
        <artifactId>runtime-domain-titan</artifactId>
        -->
        <version>0.0.1-SNAPSHOT</version>
    </dependency>

<br />

####jetty

This contains the jetty code to start the rest application in Jetty

####restlet

This is where the generated restlet resources go.

To compile and execute the rest resources the following dependency is required
This brings in [Restlet](http://restlet.org/). Restlet is the rest framework used by UMLG to expose the entities as rest resources.

         <dependency>
             <groupId>org.umlg</groupId>
             <artifactId>runtime-restlet</artifactId>
             <version>0.0.1-SNAPSHOT</version>
         </dependency>

And lastly to use the web ui add the following dependency This makes available web resources. The javascript, html and
css files needed for the UMLG admin gui.

         <dependency>
             <groupId>org.umlg</groupId>
             <artifactId>runtime-ui</artifactId>
             <version>0.0.1-SNAPSHOT</version>
         </dependency>

####war

This packages the application as a war.

<br />

##The entities

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
that it appends to the collection as opposed to the setter that replaces the collection.

    one.addToMany(many);

or just use the standard java collection 'add' method

    one.getMany().add(many);

Similarly a remover is generated

    one.removeFromMany(many);

or use the standard java collection 'remove' method

    one.getMany().remove(many);

and lastly to persist the entities call,

    UMLG.get().commit()

or rollback the transaction

    UMLG.get().rollback()

`UMLG.get()` returns an instance of `org.umlg.runtime.adaptor.UmlgGraph`. `UmlgGraph` wraps the underlying blueprints graph and implements
'com.tinkerpop.blueprints.TransactionalGraph' and 'com.tinkerpop.blueprints.KeyIndexableGraph' It is a singleton and is
always available on the current thread via `UMLG.get()`



