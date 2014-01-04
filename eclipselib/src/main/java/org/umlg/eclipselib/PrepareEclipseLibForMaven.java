package org.umlg.eclipselib;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogManager;

/**
 * Date: 2013/11/04
 * Time: 3:51 PM
 */
public class PrepareEclipseLibForMaven {

    private List<Dependency> dependencies = new ArrayList<Dependency>();

    public static void main(String[] args) throws IOException, InterruptedException {
        URL url = PrepareEclipseLibForMaven.class.getResource("/logging.properties");
        LogManager.getLogManager().readConfiguration(url.openStream());

        PrepareEclipseLibForMaven prepareEclipseLibForMaven = new PrepareEclipseLibForMaven();
        prepareEclipseLibForMaven.parsePom();
        prepareEclipseLibForMaven.copyFileToProject();
    }

    private void parsePom() {
        try {
            File frameworkPom = new File("./framework/pom.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(frameworkPom);
            doc.getDocumentElement().normalize();
            NodeList groupIds = doc.getElementsByTagName("groupId");
            for (int temp = 0; temp < groupIds.getLength(); temp++) {
                Node nNode = groupIds.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nNode;
                    if (element.getTextContent().startsWith("org.eclipse")) {
                        Element groupdId = element;
                        //Check if the previous element is exclusion, if so ignore it
                        Element exclusion = (Element) element.getParentNode();
                        if (exclusion.getTagName().equals("dependency")) {
                            Node artifactId = element.getNextSibling();
                            while (!(artifactId instanceof Element) && artifactId != null) {
                                artifactId = artifactId.getNextSibling();
                            }
                            Node version = artifactId.getNextSibling();
                            while (!(version instanceof Element) && version != null) {
                                version = version.getNextSibling();
                            }
                            this.dependencies.add(new Dependency(groupdId.getTextContent(), artifactId.getTextContent(), version.getTextContent()));
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void copyFileToProject() throws IOException, InterruptedException {
        File eclipseLib = new File("eclipselib/lib");
        FileUtils.cleanDirectory(eclipseLib);
        for (Dependency dependency : this.dependencies) {
            dependency.copyToProject(new File("/home/pieter/.m2/repository"), eclipseLib);
        }
    }
}
