package org.tuml.javageneration.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.uml2.uml.MultiplicityElement;
import org.tuml.ocl.StandaloneFacade;

import tudresden.ocl20.pivot.parser.ParseException;
import tudresden.ocl20.pivot.pivotmodel.Constraint;
import tudresden.ocl20.pivot.tools.codegen.exception.Ocl2CodeException;
import tudresden.ocl20.pivot.tools.codegen.ocl2java.IOcl2JavaSettings;
import tudresden.ocl20.pivot.tools.codegen.ocl2java.Ocl2JavaFactory;

public class OclUtil {

	public static String oclToJava(File oclFile) {
		try {
			List<Constraint> constraintList = StandaloneFacade.INSTANCE.parseOclConstraints(StandaloneFacade.INSTANCE.getModel(), oclFile);
			IOcl2JavaSettings settings = Ocl2JavaFactory.getInstance().createJavaCodeGeneratorSettings();
			settings.setGettersForPropertyCallsEnabled(true);
			String java = StandaloneFacade.INSTANCE.generateJavaCode(constraintList, settings).get(0);
			java = java.replace("aClass.", "");

			StringBuilder sb = new StringBuilder();
			String[] lines = java.split("\n");
			int i = 1;
			for (String s : lines) {
				if (lines.length == i++) {
					sb.append("return ");
					sb.append(s);
				} else {
					sb.append(s);
					sb.append("\n");
				}
			}
			return sb.toString();
		} catch (Ocl2CodeException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String getCollectionInterface(MultiplicityElement multiplicityElement) {
		if (multiplicityElement.isUnique() && multiplicityElement.isOrdered()) {
			return "OrderedSet";
		} else if (multiplicityElement.isUnique() && !multiplicityElement.isOrdered()) {
			return "Set";
		} else if (!multiplicityElement.isUnique() && !multiplicityElement.isOrdered()) {
			return "Bag";
		} else if (!multiplicityElement.isUnique() && multiplicityElement.isOrdered()) {
			return "Sequence";
		} else {
			throw new IllegalStateException("Not supported");
		}
	}
	
}
