package org.tuml.javageneration;

import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.visitor.clazz.*;
import org.tuml.javageneration.visitor.enumeration.EnumerationVisitor;
import org.tuml.javageneration.visitor.enumeration.TofromJsonForEnumCreator;
import org.tuml.javageneration.visitor.interfaze.InterfaceVisitor;
import org.tuml.javageneration.visitor.model.RootEntryPointCreator;
import org.tuml.javageneration.visitor.operation.OperationImplementorSimple;
import org.tuml.javageneration.visitor.property.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 2012/12/28
 * Time: 10:32 AM
 *
 * This visitor requires tumlib to be in the model
 */
public class TumlLibVisitors {

    private static final String META_SOURCE_FOLDER = "src/main/generated-java-meta";

    public static List<Visitor<?>> getDefaultJavaVisitors() {
        List<Visitor<?>> result = new ArrayList<Visitor<?>>();
        result.add(new MetaClassBuilder(Workspace.INSTANCE, META_SOURCE_FOLDER));
        return result;
    }

}
