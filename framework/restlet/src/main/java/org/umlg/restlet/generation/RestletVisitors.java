package org.umlg.restlet.generation;

import java.util.ArrayList;
import java.util.List;

import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.DefaultVisitors;
import org.umlg.restlet.router.RestletRouterEnumGenerator;
import org.umlg.restlet.visitor.clazz.*;
import org.umlg.restlet.visitor.model.*;

public class RestletVisitors {

    private static final String RESTLET_SOURCE_FOLDER = "src/main/generated-java-restlet";

    public static List<Visitor<?>> getDefaultJavaVisitors() {
        List<Visitor<?>> result = new ArrayList<Visitor<?>>();
        result.addAll(DefaultVisitors.getDefaultJavaVisitors());
        result.add(new RestletRouterEnumGenerator(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new EntityServerResourceBuilder(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new EntityForLookupServerResourceBuilder(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));

        //TODO remove this after testing calling OPTIONS on EntityServerResource
//        result.add(new MetaDataResourceBuilder(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));

//        result.add(new NavigatePropertyServerResourceBuilder(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new NavigatePropertyOverloadedPostServerResourceBuilder(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new NavigatePropertyOverloadedPostForLookupServerResourceBuilder(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));

        result.add(new AssociationClassOverloadedPostServerResourceBuilder(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));

//        result.add(new RootResourceServerResourceBuilder(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new RootOverLoadedPostResourceServerResourceBuilder(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new RootOverLoadedPostForLookupResourceServerResourceBuilder(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));

        result.add(new AppResourceServerResourceBuilder(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new AddIdLiteralsToRuntimeEnum(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new AddUmlgUriFieldToRuntimePropertyEnum(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new AddUmlgMetaDataUriFieldToRuntimePropertyEnum(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));

        //This must be before AddUmlgMetaDataUriFieldToRootRuntimePropertyEnum and AddUriToRootRuntimePropertyEnum
        //In order for the tumlMetaDataUri and uri to be added to the literal's constructor
        result.add(new AddIdLiteralsToRootRuntimeEnum(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));

        result.add(new AddUmlgMetaDataUriFieldToRootRuntimePropertyEnum(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new AddUriToRootRuntimePropertyEnum(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));

        result.add(new AddFieldTypeFieldToRuntimeLiteral(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new AddFieldTypeFieldToRootRuntimeLiteral(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new AddUmlgLookupUriToRuntimePropertyEnum(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new AddUmlgLookupCompositeParentUriToRuntimePropertyEnum(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new LookupForOneResourceBuilder(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new LookupForManyResourceBuilder(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
//        result.add(new LookupCompositeParentResourceBuilder(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
//        result.add(new LookupOnCompositeParentResourceBuilder(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
//        result.add(new LookupOnCompositeParentCompositeParentResourceBuilder(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new CompositePathServerResourceBuilder(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new EnumLookupResourceServerResourceBuilder(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new UmlgRestletNodeBuilder(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new QueryExecuteResourceBuilder(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
//        result.add(new TransactionResourceRouterAdder(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));

        result.add(new RestletComponentAndApplicationGenerator(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));
        result.add(new RestletFromJsonCreator(Workspace.INSTANCE, RESTLET_SOURCE_FOLDER));



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
