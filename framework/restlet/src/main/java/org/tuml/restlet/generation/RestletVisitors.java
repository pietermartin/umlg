package org.tuml.restlet.generation;

import java.util.ArrayList;
import java.util.List;

import org.tuml.framework.Visitor;
import org.tuml.generation.Workspace;
import org.tuml.javageneration.DefaultVisitors;
import org.tuml.restlet.router.RestletRouterEnumGenerator;
import org.tuml.restlet.visitor.clazz.AddUriToRootRuntimePropertyEnum;
import org.tuml.restlet.visitor.clazz.AddUriToRuntimePropertyEnum;
import org.tuml.restlet.visitor.clazz.AppResourceServerResourceBuilder;
import org.tuml.restlet.visitor.clazz.EntityServerResourceBuilder;
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
		result.add(new AddUriToRuntimePropertyEnum(Workspace.INSTANCE));
		result.add(new AddUriToRootRuntimePropertyEnum(Workspace.INSTANCE));
		return result;
	}
	
}
