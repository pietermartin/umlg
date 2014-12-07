<!-- SqlG Documentation -->

##Introduction

<kbd>Sqlg</kbd> is a implementation of [Tinkerpop3](https://github.com/tinkerpop/tinkerpop3) on a [RDBMS](http://en.wikipedia.org/wiki/Relational_database_management_system).
Currently [HSQLDB](http://hsqldb.org/) and [postgresql](http://www.postgresql.org/) is supported.

##Getting Started

The <kbd>Sqlg</kbd> maven coordinates for <kbd>HSQLDB</kbd> is

    <dependency>
        <groupId>org.umlg</groupId>
        <artifactId>sqlg-hsqldb</artifactId>
        <version>0.0.2-SNAPSHOT</version>
    </dependency>

and for <kbd>postgresql</kbd>

    <dependency>
        <groupId>org.umlg</groupId>
        <artifactId>sqlg-postgres</artifactId>
        <version>0.0.2-SNAPSHOT</version>
    </dependency>

<kbd>Sqlg</kbd> needs one property file <kbd>sqlg.properties</kbd>

For HSQLDB a sample is

    jdbc.url=jdbc:hsqldb:file:/tmp/sqlg
    jdbc.username=SA
    jdbc.password=

and for postgresql

    jdbc.url=jdbc:postgresql://localhost:5432/sqlgraphdb
    jdbc.username=postgres
    jdbc.password=postgres

###Gremlin Console

