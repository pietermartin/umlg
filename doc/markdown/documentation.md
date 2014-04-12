#Quick Preview

Here is a simple uml class diagram.

![image of person works for company](images/uml/Package_umlg_demoQuickPreviewClassDiagram.PNG)

Here are the generated entities in action.

    Company company = new Company();
    company.setName("UMLG");
    Person person = new Person();
    person.setFirstname("Joe");
    person.setLastname("Bloggs");
    company.getEmployee().add(person);
    UMLG.get().commit();
    Assert.assertTrue(company.getEmployee().contains(person));

#Introduction

A short [introduction](introduction.html) to UMLG.

#Getting Started

Quickly get a UMLG application up and running with the [getting started guide](getting_started.html).

#UMLG Manual

The UML [manual](umlg_reference.html) is organized as a reference document. Going through all uml class diagram features supported and implemented by UMLG.

#OCL Reference

An [OCL reference document](ocl_reference.html) illustrating OCL as supported in UMLG.