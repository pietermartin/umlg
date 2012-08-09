package org.tuml.tumltest;

import java.io.File;

import org.tuml.generation.JavaGenerator;
import org.tuml.javageneration.DefaultAuditVisitors;

public class TestAuditGeneration {

	public static void main(String[] args) {
		JavaGenerator javaGenerator = new JavaGenerator();
		javaGenerator.generate(new File("src/main/model/tinker-test.uml"), new File("/home/pieter/workspace-tuml/tuml/test/tuml-test-audit"),
				DefaultAuditVisitors.getDefaultJavaVisitors());
	}
	
}
