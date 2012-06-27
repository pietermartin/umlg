package org.tuml.ocl.test;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.junit.Assert;
import org.junit.Test;
import org.tuml.ocl.StandaloneFacade;

import tudresden.ocl20.pivot.model.IModel;
import tudresden.ocl20.pivot.pivotmodel.Constraint;
import tudresden.ocl20.pivot.tools.codegen.ocl2java.IOcl2JavaSettings;
import tudresden.ocl20.pivot.tools.codegen.ocl2java.Ocl2JavaFactory;

public class TestOcl {

	@Test
	public void testOcl() throws Exception {
		File rlModel = new File("src/main/resources/model/royalsandloyals.uml");
		File rlOclConstraints = new File("src/main/resources/constraints/rl_allConstraints.ocl");
		StandaloneFacade.INSTANCE.initialize(new URL("file:" + new File("src/main/resources/log4j.properties").getAbsolutePath()));
		String absolutePath = new File("../../eclipselib/org/eclipse/uml2/uml/resources/3.1.100-v201008191510/resources-3.1.100-v201008191510.jar").getAbsolutePath();
		URI uri = URI.createURI("jar:file:" + absolutePath + "!/");
		IModel model = StandaloneFacade.INSTANCE.loadUMLModel(rlModel, uri);
		List<Constraint> constraintList = StandaloneFacade.INSTANCE.parseOclConstraints(model, rlOclConstraints);
		IOcl2JavaSettings settings = Ocl2JavaFactory.getInstance().createJavaCodeGeneratorSettings();
		settings.setSourceDirectory("src/main/generated-ocl");
		settings.setGettersForPropertyCallsEnabled(true);
		List<String> result = StandaloneFacade.INSTANCE.generateJavaCode(constraintList, settings);
		Assert.assertTrue(!result.isEmpty());
	}
	
}
