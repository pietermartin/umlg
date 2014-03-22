package org.umlg.javageneration.visitor.model;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Property;
import org.umlg.framework.ModelLoader;
import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.visitor.BaseVisitor;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date: 2014/03/01
 * Time: 2:46 PM
 * @deprecated
 */
public class GremlinGroovyModelVisitor extends BaseVisitor implements Visitor<Model> {

    public GremlinGroovyModelVisitor(Workspace workspace, String sourceDir) {
        super(workspace, sourceDir);
    }

    @Override
    public void visitBefore(Model model) {
        Configuration cfg = new Configuration();
        try {
            cfg.setClassForTemplateLoading(getClass(), "");
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
            cfg.setIncompatibleImprovements(new Version(2, 3, 20));
            Template temp = cfg.getTemplate("gremlinGroovyPropertyTemplate.ftl");
            ByteArrayOutputStream groovyOut = new ByteArrayOutputStream();
            Writer out = new OutputStreamWriter(groovyOut);
            List<GremlinProperty> gremlinProperties = new ArrayList<GremlinProperty>();
            Map<String, List<GremlinProperty>> propertiesMap = new HashMap<String, List<GremlinProperty>>();
            for (Property p : ModelLoader.INSTANCE.getAllProperties()) {
                gremlinProperties.add(new GremlinProperty(p.getQualifiedName()));
            }
            propertiesMap.put("properties", gremlinProperties);
            temp.process(propertiesMap, out);
            out.flush();
            this.addToGroovySource("org.umlg.gremlin.groovy.UmlgGremlinGroovyGraphPropertyNames", groovyOut.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void visitAfter(Model element) {
    }
}
