package org.tuml.testocl;

import java.io.File;

import org.tuml.generation.JavaGenerator;
import org.tuml.javageneration.DefaultVisitors;

public class TestOclGeneration {

	public static void main(String[] args) {
		JavaGenerator javaGenerator = new JavaGenerator();
		javaGenerator.generate(new File("src/main/model/test-ocl.uml"), new File("/home/pieter/workspace-tuml/tuml/test/tuml-test-ocl"),
				DefaultVisitors.getDefaultJavaVisitors());
	}
	
}
