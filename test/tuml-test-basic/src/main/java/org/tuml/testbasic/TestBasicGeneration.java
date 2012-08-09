package org.tuml.testbasic;

import java.io.File;

import org.tuml.generation.JavaGenerator;
import org.tuml.javageneration.DefaultVisitors;

public class TestBasicGeneration {

	public static void main(String[] args) {
		JavaGenerator javaGenerator = new JavaGenerator();
		javaGenerator.generate(new File("src/main/model/tinker-test-basic.uml"), new File("/home/pieter/workspace-tuml/tuml/test/tuml-test-basic"),
				DefaultVisitors.getDefaultJavaVisitors());
	}

}
