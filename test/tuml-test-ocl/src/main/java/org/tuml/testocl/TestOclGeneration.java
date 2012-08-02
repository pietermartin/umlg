package org.tuml.testocl;

import org.tuml.javageneration.JavaGenerator;

public class TestOclGeneration {

	public static void main(String[] args) {
		JavaGenerator.main(new String[] { "src/main/model/test-ocl.uml", "/home/pieter/workspace-tuml/tuml/test/tuml-test-ocl", "false" });
	}
	
}
