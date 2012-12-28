package org.tuml.test.generation;

import java.io.File;

import org.tuml.generation.JavaGenerator;
import org.tuml.javageneration.DefaultVisitors;
import org.tuml.javageneration.TumlLibVisitors;
import org.tuml.restlet.generation.RestletVisitors;

public class GenerateTestProjects {

	public static void main(String[] args) {
        if (args.length == 0) {
            args = new String[]{"/home/pieter/workspace-tuml/tuml/"};
        }
		JavaGenerator javaGenerator = new JavaGenerator();
		javaGenerator.generate(new File(args[0] + "/test/tuml-test/src/main/model/tinker-test.uml"), new File(args[0] + "/test/tuml-test/"), DefaultVisitors.getDefaultJavaVisitors(), TumlLibVisitors.getDefaultJavaVisitors());
		javaGenerator = new JavaGenerator();
		javaGenerator.generate(new File(args[0] + "/test/test-restlet/src/main/model/restANDjson.uml"), new File(args[0] + "/test/test-restlet/"), RestletVisitors.getDefaultJavaVisitors());
		javaGenerator = new JavaGenerator();
		javaGenerator.generate(new File(args[0] + "/test/tuml-test-basic/src/main/model/tinker-test-basic.uml"), new File(args[0] + "/test/tuml-test-basic/"), DefaultVisitors.getDefaultJavaVisitors(), TumlLibVisitors.getDefaultJavaVisitors());
		javaGenerator = new JavaGenerator();
		javaGenerator.generate(new File(args[0] + "/test/tuml-test-ocl/src/main/model/test-ocl.uml"), new File(args[0] + "/test/tuml-test-ocl/"), DefaultVisitors.getDefaultJavaVisitors());
	}
}
