package org.tuml.restlet.generation;

import java.util.ArrayList;
import java.util.List;

import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.DefaultVisitors;
import org.tuml.restlet.router.RestletRouterEnumGenerator;
import org.tuml.restlet.visitor.clazz.AddFieldTypePropertyToRootRuntimeLiteral;
import org.tuml.restlet.visitor.clazz.AddFieldTypePropertyToRuntimeLiteral;
import org.tuml.restlet.visitor.clazz.AddIdLiteralsToRootRuntimeEnum;
import org.tuml.restlet.visitor.clazz.AddIdLiteralsToRuntimeEnum;
import org.tuml.restlet.visitor.clazz.AddTumlLookupCompositeParentUriToRuntimePropertyEnum;
import org.tuml.restlet.visitor.clazz.AddTumlLookupUriToRuntimePropertyEnum;
import org.tuml.restlet.visitor.clazz.AddTumlUriFieldToRuntimePropertyEnum;
import org.tuml.restlet.visitor.clazz.AddUriToRootRuntimePropertyEnum;
import org.tuml.restlet.visitor.clazz.AppResourceServerResourceBuilder;
import org.tuml.restlet.visitor.clazz.CompositePathServerResourceBuilder;
import org.tuml.restlet.visitor.clazz.EntityServerResourceBuilder;
import org.tuml.restlet.visitor.clazz.LookupCompositeParentResourceBuilder;
import org.tuml.restlet.visitor.clazz.LookupOnCompositeParentCompositeParentResourceBuilder;
import org.tuml.restlet.visitor.clazz.LookupOnCompositeParentResourceBuilder;
import org.tuml.restlet.visitor.clazz.LookupResourceBuilder;
import org.tuml.restlet.visitor.clazz.NavigatePropertyServerResourceBuilder;
import org.tuml.restlet.visitor.clazz.RootResourceServerResourceBuilder;

public class RestletVisitors {

	public static List<Visitor<?>> getDefaultJavaVisitors() {
		List<Visitor<?>> result = new ArrayList<Visitor<?>>();
		result.addAll(DefaultVisitors.getDefaultJavaVisitors());
		result.add(new RestletRouterEnumGenerator(Workspace.INSTANCE));
		result.add(new EntityServerResourceBuilder(Workspace.INSTANCE));
		result.add(new NavigatePropertyServerResourceBuilder(Workspace.INSTANCE));
		result.add(new RootResourceServerResourceBuilder(Workspace.INSTANCE));
		result.add(new AppResourceServerResourceBuilder(Workspace.INSTANCE));
		result.add(new AddIdLiteralsToRuntimeEnum(Workspace.INSTANCE));
		result.add(new AddTumlUriFieldToRuntimePropertyEnum(Workspace.INSTANCE));
		result.add(new AddIdLiteralsToRootRuntimeEnum(Workspace.INSTANCE));
		result.add(new AddUriToRootRuntimePropertyEnum(Workspace.INSTANCE));
		result.add(new AddFieldTypePropertyToRuntimeLiteral(Workspace.INSTANCE));
		result.add(new AddFieldTypePropertyToRootRuntimeLiteral(Workspace.INSTANCE));
		result.add(new AddTumlLookupUriToRuntimePropertyEnum(Workspace.INSTANCE));
		result.add(new AddTumlLookupCompositeParentUriToRuntimePropertyEnum(Workspace.INSTANCE));
		result.add(new LookupResourceBuilder(Workspace.INSTANCE));
		result.add(new LookupCompositeParentResourceBuilder(Workspace.INSTANCE));
		result.add(new LookupOnCompositeParentResourceBuilder(Workspace.INSTANCE));
		result.add(new LookupOnCompositeParentCompositeParentResourceBuilder(Workspace.INSTANCE));
		result.add(new CompositePathServerResourceBuilder(Workspace.INSTANCE));
		return result;
	}
	
}
