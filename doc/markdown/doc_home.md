<!-- UMLG Documentation -->

##Quick Preview

Here is a simple uml class diagram.

![image of person works for company](images/uml/Package_umlg_demo1ClassDiagram.PNG)

Here are the generated entities in action.

    Company company = new Company();
    company.setName("UMLG");
    Person person = new Person();
    person.setFirstname("Joe");
    person.setLastname("Bloggs");
    company.getEmployee().add(person);
    UMLG.get().commit();
    Assert.assertTrue(company.getEmployee().contains(person));

##Introduction

A short [introduction](introduction.html) to UMLG.

##Getting Started

Quickly get a UMLG application up and running with the [getting started guide](getting_started.html).

##UMLG In Action

An in depth explanation of the entities and the primary UMLG db interface. [UMLG In Action](umlg_inaction.html).

##UMLG Gremlin Extension

UMLG's [gremlin extension](gremlin_extension.html) to facilitate accessing properties and association classes.

##UMLG UMLL Reference

A UMLG [reference manual](umlg_reference.html). Going through all uml class diagram features supported and implemented by UMLG.

##OCL Reference

An [OCL reference document](ocl_reference.html) illustrating OCL as supported in UMLG.

##The Rest Interface

Description of UMLG's  [rest interface](rest_interface.html).

##The Gui

Description of UMLG's [gui](gui.html).