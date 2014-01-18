package org.umlg.tumltest;

import java.io.File;

import org.umlg.generation.JavaGenerator;
import org.umlg.javageneration.DefaultAuditVisitors;

public class TestAuditGeneration {

	public static void main(String[] args) {
		JavaGenerator javaGenerator = new JavaGenerator();
		javaGenerator.generate(new File("src/main/model/tests-test.uml"), new File("/home/pieter/workspace-umlg/umlg/test/umlg-test-audit"),
				DefaultAuditVisitors.getDefaultJavaVisitors());
	}
	
}
