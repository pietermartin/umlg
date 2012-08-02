package org.tuml.testbasic;

import org.tuml.javageneration.JavaGenerator;

public class TestBasicGeneration {

	public static void main(String[] args) {
		JavaGenerator.main(new String[] { "src/main/model/tinker-test-basic.uml", "/home/pieter/workspace-tuml/tuml/test/tuml-test-basic", "false" });
	}
	
}
