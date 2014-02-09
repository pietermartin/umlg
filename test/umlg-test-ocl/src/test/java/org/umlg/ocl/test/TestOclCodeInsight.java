package org.umlg.ocl.test;

import org.eclipse.ocl.helper.Choice;
import org.eclipse.uml2.uml.Classifier;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.umlg.framework.ModelLoader;
import org.umlg.ocl.UmlgOcl2Parser;
import org.umlg.ocl.UmlgOclExecutor;
import org.umlg.qualifier.Bank;
import org.umlg.qualifier.Employee;
import org.umlg.runtime.collection.UmlgOrderedSet;
import org.umlg.runtime.test.BaseLocalDbTest;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.logging.LogManager;

/**
 * Date: 2014/02/09
 * Time: 7:11 PM
 */
public class TestOclCodeInsight extends BaseLocalDbTest {

    @BeforeClass
    public static void beforeClass() {
        try {
            URL url = BaseLocalDbTest.class.getResource("/logging.properties");
            LogManager.getLogManager().readConfiguration(url.openStream());
            URL umlUrl = BaseLocalDbTest.class.getResource("/test-ocl.uml");
            UmlgOcl2Parser.INSTANCE.init(new File(umlUrl.toURI()));
            @SuppressWarnings("unused")
            UmlgOcl2Parser instance = UmlgOcl2Parser.INSTANCE;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void test() {
        Bank bank = new Bank(true);
        bank.setName("thebank");
        Employee employee1 = new Employee(true);
        employee1.setName("employee1");
        bank.addToEmployee(employee1);
        Employee employee2 = new Employee(true);
        employee2.setName("employee2");
        bank.addToEmployee(employee2);
        Employee employee3 = new Employee(true);
        employee3.setName("employee3");
        bank.addToEmployee(employee3);
        db.commit();


        Classifier contextClassifier = (Classifier) ModelLoader.INSTANCE.findNamedElement(bank.getQualifiedName());
        UmlgOcl2Parser.INSTANCE.getHelper().setContext(contextClassifier);
        List<Choice> insights = UmlgOcl2Parser.INSTANCE.getHelper().getSyntaxHelp(null, "self.");
        for (Choice choice : insights) {
            System.out.println(choice);
        }
    }
}
