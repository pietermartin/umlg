#Introduction

UMLG is a UML to java code generator. From class diagrams, persistent java entities are generated. The entities persist
via an embedded [Blueprints](http://blueprints.tinkerpop.com) graph database. The semantics of a
[Property Graph Model](https://github.com/tinkerpop/blueprints/wiki/Property-Graph-Model) fits naturally for
implementing the rich semantics of UML class diagrams in java.

One of the primary objectives of UMLG is to implement the [UML2](http://www.omg.org/spec/UML/2.4.1/Superstructure/PDF)
semantics as accurately as possible.

UML has strong and detailed semantics for specifying structural features. In general it is far easier and quicker to
specify the complex structure and relationships of domain entities in UML. UMLG makes that specification much more than
just documentation. A large part of the lack of popularity of UML is that it remains only documentation. Many UML tools
can generate java, however it is seldom if ever generates persistent entities.

UMLG entities are **not** [POJOs](http://en.wikipedia.org/wiki/Plain_Old_Java_Object). They are always persistent objects.
Interceptors are not used in the implementation and they contain no annotations. The only configuration required is one
property specifying the location of the underlying graph database. As such UMLG entities always execute, (unit test or
production) in an equivalent environment. Many graph databases support an in memory only option. If this is used UMLG's
entities will reside in memory only.

UMLG's basic pattern is that an entity wraps a vertex and associations between entities are realized as edges between vertices.

Graph databases have very fast startup times that do not increase with the number of entities (vertices and edges).
Even on large projects with thousands of entities UMLG will still start up in milliseconds. No need for mock tests nor
integration tests.

Graph databases are very good at traversing relationships. It's their strength after all. This translates to UMLG entities
being very efficient and fast at navigating object oriented associations. (//TODO do a simple JPA comparison)

UMLG implement most class diagram constructs. This includes: inheritance, interfaces, abstract
classes, associations, composition, complex data types, multiplicity, collection types (Set, OrderedSet, Sequence, Bag),
qualifiers, constraints.

UMLG has support for [OCL](href="http://www.omg.org/spec/OCL/2.3.1/PDF") (Object Constraint Language). OCL is a
powerful constraint and query language that operates directly on the entities. OCL constraints and queries can be specified
in the model at design time or executed runtime.

UMLG can also, optionally, generate a rest interface for performing crud and query operations. A web interface is provided
to perform crud operations and execute queries. Queries can be executed in [OCL](http://www.omg.org/spec/OCL/2.3.1/PDF),
[Gremlin](https://github.com/tinkerpop/gremlin/wiki) or in the underlying graph databases' native query language.

Currently, [Bitsy](https://bitbucket.org/lambdazen/bitsy/wiki/Home), [Neo4j](http://www.neo4j.org/) and
[Titan](https://github.com/thinkaurelius/titan/wiki) are supported as the underlying blueprints graph databases.