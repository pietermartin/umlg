<!-- UMLG In Action -->

##UmlgGraph interface

`UmlgGraph` is UMLG's primary interface to the underlying graph db. It extends both `com.tinkerpop.blueprints.TransactionalGraph` and `com.tinkerpop.blueprints.KeyIndexableGraph`.
Some important additional methods on it are,

    <T extends PersistentObject> T instantiateClassifier(Object id);

    <T extends PersistentObject> T getFromUniqueIndex(String key, Object value);

    <T extends PersistentObject> List<T> getFromIndex(String key, Object value);

    String executeQueryToString(UmlgQueryEnum umlgQueryEnum, Object contextId, String query);

    Object executeQuery(UmlgQueryEnum umlgQueryEnum, Object contextId, String query);

`instantiateClassifier` retrieves an entity from the db given its vertex id.

`getFromUniqueIndex` retrieves an entity that has been uniquely indexed for a key and value of one of is properties.
The key is the qualified name of the property.

`getFromIndex` is the same as `getFromUniqueIndex` except that the index is not unique and thus returns a List

`executeQueryToString` executes a query and returns the result as a `String`. UmlgQueryEnum can be OCL, GROOVY or NATIVE,
UmlgQueryEnum.GROOVY includes Gremlin queries.

`executeQuery` executes a query returning the raw result. In the case of OCL the raw result will be your entities.


##Indexing

##OCL

##Gremlin

##Cypher
