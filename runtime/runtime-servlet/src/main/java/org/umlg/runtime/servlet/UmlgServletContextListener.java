package org.umlg.runtime.servlet;

import org.umlg.ocl.UmlgOcl2Parser;
import org.umlg.runtime.util.UmlgProperties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Date: 2014/04/08
 * Time: 7:10 AM
 */
public class UmlgServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        UmlgOcl2Parser.INSTANCE.init(UmlgProperties.INSTANCE.getModelFileName() + ".uml");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}
