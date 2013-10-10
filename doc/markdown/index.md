Umlg Home Page Under Construction
==============

Introduction
------------

Umlg is a uml to java code generator. From uml class diagrams, java domain entities are generated. The entities persist in a graph database. For the persistence [Tinkerpop Blueprints](http://blueprints.tinkerpop.com/) is used. The semantics of a [Property Graph Model](https://github.com/tinkerpop/blueprints/wiki/Property-Graph-Model) is a natural fit for implementing the rich semantics of UML class diagrams.

In general, a entity wraps a vertex and associations between entities are realized as edges between vertexes.

Umlg uses the [eclipse uml2 project](http://projects.eclipse.org/projects/modeling.mdt.uml2) to load the uml model into memory. [Eclipse Papyrus](http://projects.eclipse.org/projects/modeling.mdt.papyrus) is the recommended uml2 modelling tool.

A lot of care has been taken to implement the java domain model semantics as close as possible to the [UML2 specification] (http://www.omg.org/spec/UML/2.4.1/Superstructure/PDF)

Umlg can also, optionally, generate a rest interface to expose the domain model for performing crud and query operations. On top of it there is a web interface that makes it easy to perform crud and query the data. Queries can be executed in [OCL](http://www.omg.org/spec/OCL/2.3.1/PDF), [Gremlin](https://github.com/tinkerpop/gremlin/wiki) or the underlying graph databases' native query language.

Go to [demo.uml.org](http://demo.umlg.org:8111/restAndJson/ui2) to have a look.

Currently, [Bitsy](https://bitbucket.org/lambdazen/bitsy/wiki/Home), [OrientDb](http://www.orientdb.org/), [Neo4j](http://www.neo4j.org/) and [Titan](https://github.com/thinkaurelius/titan/wiki) is supported as underlying blueprints graph databases.

Getting Started
---------------

[Getting Started](http://www.umlg.org/gettingStarted.html)

The Rest
--------
