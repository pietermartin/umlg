package org.umlg.restlet.generation;

import org.umlg.framework.Visitor;
import org.umlg.generation.Workspace;
import org.umlg.javageneration.DefaultVisitors;
import org.umlg.restlet.router.RestletRouterEnumGenerator;
import org.umlg.restlet.visitor.clazz.*;
import org.umlg.restlet.visitor.model.*;

import java.util.ArrayList;
import java.util.List;

public class RestletVisitors {

    public static List<Visitor<?>> getDefaultJavaVisitors() {
        List<Visitor<?>> result = new ArrayList<Visitor<?>>();
        result.addAll(DefaultVisitors.getDefaultJavaVisitors());
        result.add(new RestletRouterEnumGenerator(Workspace.INSTANCE, Workspace.RESTLET_SOURCE_FOLDER));
        result.add(new EntityServerResourceBuilder(Workspace.INSTANCE, Workspace.RESTLET_SOURCE_FOLDER));
        result.add(new EntityForLookupServerResourceBuilder(Workspace.INSTANCE, Workspace.RESTLET_SOURCE_FOLDER));

        result.add(new NavigatePropertyOverloadedPostServerResourceBuilder(Workspace.INSTANCE, Workspace.RESTLET_SOURCE_FOLDER));
        result.add(new NavigatePropertyOverloadedPostForLookupServerResourceBuilder(Workspace.INSTANCE, Workspace.RESTLET_SOURCE_FOLDER));

        result.add(new AssociationClassOverloadedPostServerResourceBuilder(Workspace.INSTANCE, Workspace.RESTLET_SOURCE_FOLDER));

        result.add(new RootOverLoadedPostResourceServerResourceBuilder(Workspace.INSTANCE, Workspace.RESTLET_SOURCE_FOLDER));
        result.add(new RootOverLoadedPostForLookupResourceServerResourceBuilder(Workspace.INSTANCE, Workspace.RESTLET_SOURCE_FOLDER));

        result.add(new AppResourceServerResourceBuilder(Workspace.INSTANCE, Workspace.RESTLET_SOURCE_FOLDER));
        result.add(new AddIdLiteralsToRuntimeEnum(Workspace.INSTANCE, Workspace.RESTLET_SOURCE_FOLDER));
        result.add(new AddUmlgUriFieldToRuntimePropertyEnum(Workspace.INSTANCE, Workspace.RESTLET_SOURCE_FOLDER));
        result.add(new AddUmlgMetaDataUriFieldToRuntimePropertyEnum(Workspace.INSTANCE, Workspace.RESTLET_SOURCE_FOLDER));

        //In order for the tumlMetaDataUri and uri to be added to the literal's constructor
        result.add(new AddIdLiteralsToRootRuntimeEnum(Workspace.INSTANCE, Workspace.RESTLET_SOURCE_FOLDER));

        result.add(new AddUmlgMetaDataUriFieldToRootRuntimePropertyEnum(Workspace.INSTANCE, Workspace.RESTLET_SOURCE_FOLDER));
        result.add(new AddUriToRootRuntimePropertyEnum(Workspace.INSTANCE, Workspace.RESTLET_SOURCE_FOLDER));

        result.add(new AddFieldTypeFieldToRuntimeLiteral(Workspace.INSTANCE, Workspace.RESTLET_SOURCE_FOLDER));
//        result.add(new AddFieldTypeFieldToRootRuntimeLiteral(Workspace.INSTANCE, Workspace.RESTLET_SOURCE_FOLDER));
        result.add(new AddUmlgLookupUriToRuntimePropertyEnum(Workspace.INSTANCE, Workspace.RESTLET_SOURCE_FOLDER));
        result.add(new AddUmlgLookupCompositeParentUriToRuntimePropertyEnum(Workspace.INSTANCE, Workspace.RESTLET_SOURCE_FOLDER));
        result.add(new LookupForOneResourceBuilder(Workspace.INSTANCE, Workspace.RESTLET_SOURCE_FOLDER));
        result.add(new LookupForManyResourceBuilder(Workspace.INSTANCE, Workspace.RESTLET_SOURCE_FOLDER));
        result.add(new CompositePathServerResourceBuilder(Workspace.INSTANCE, Workspace.RESTLET_SOURCE_FOLDER));
        result.add(new EnumLookupResourceServerResourceBuilder(Workspace.INSTANCE, Workspace.RESTLET_SOURCE_FOLDER));
        result.add(new UmlgRestletNodeBuilder(Workspace.INSTANCE, Workspace.RESTLET_SOURCE_FOLDER));
        result.add(new QueryExecuteResourceBuilder(Workspace.INSTANCE, Workspace.RESTLET_SOURCE_FOLDER));

        result.add(new RestletComponentAndApplicationGenerator(Workspace.INSTANCE, Workspace.RESTLET_SOURCE_FOLDER));
        result.add(new RestletFromJsonCreator(Workspace.INSTANCE, Workspace.RESTLET_SOURCE_FOLDER));

        result.add(new DiagramPackageResourceBuilder(Workspace.INSTANCE, Workspace.RESTLET_SOURCE_FOLDER));

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
