package org.tuml.tumltest;

import java.io.File;

import org.tuml.generation.JavaGenerator;
import org.tuml.javageneration.DefaultVisitors;

public class GenerateTumlTest {

	public static void main(String[] args) {
		JavaGenerator javaGenerator = new JavaGenerator();
		javaGenerator.generate(new File("test/tuml-test/src/main/model/tinker-test.uml"), new File("./test/tuml-test"), DefaultVisitors.getDefaultJavaVisitors());
	}

}
