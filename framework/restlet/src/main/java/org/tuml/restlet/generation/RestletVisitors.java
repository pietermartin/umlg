package org.tuml.restlet.generation;

import java.util.ArrayList;
import java.util.List;

import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.DefaultVisitors;
import org.tuml.restlet.router.RestletRouterEnumGenerator;
import org.tuml.restlet.visitor.clazz.*;
import org.tuml.restlet.visitor.model.*;

public class RestletVisitors {

    private static final String RESTLET_SOURCE_FOLDER = "src/main/generated-java-restlet";

    public static List<Visitor<?>> getDefaultJavaVisitors() {
        List<Visitor<?>> result = new ArrayList<Visitor<?>>();
        result.addAll(DefaultVisitors.getDefaultJavaVisitors());
        result.add(new RestletRouterEnumGenerator(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new EntityServerResourceBuilder(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new NavigatePropertyServerResourceBuilder(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new NavigatePropertyOverloadedPostServerResourceBuilder(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new RootResourceServerResourceBuilder(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new AppResourceServerResourceBuilder(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new AddIdLiteralsToRuntimeEnum(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new AddTumlUriFieldToRuntimePropertyEnum(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new AddTumlMetaDataUriFieldToRuntimePropertyEnum(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new AddIdLiteralsToRootRuntimeEnum(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new AddUriToRootRuntimePropertyEnum(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new AddFieldTypeFieldToRuntimeLiteral(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new AddFieldTypeFieldToRootRuntimeLiteral(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new AddTumlLookupUriToRuntimePropertyEnum(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new AddTumlLookupCompositeParentUriToRuntimePropertyEnum(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new LookupResourceBuilder(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
//        result.add(new LookupCompositeParentResourceBuilder(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
//        result.add(new LookupOnCompositeParentResourceBuilder(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
//        result.add(new LookupOnCompositeParentCompositeParentResourceBuilder(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new CompositePathServerResourceBuilder(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new EnumLookupResourceServerResourceBuilder(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new TumlRestletNodeBuilder(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new QueryExecuteResourceBuilder(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new TransactionResourceRouterAdder(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));

        result.add(new RestletComponentAndApplicationGenerator(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));


        return result;
    }

    public static boolean containsVisitorForClass(Class<?> v) {
        for (Visitor<?> visitor : getDefaultJavaVisitors()) {
            if (visitor.getClass().equals(v)) {
                return true;
            }
        }
        return false;
    }

}
