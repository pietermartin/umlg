package org.tuml.test.generation;

import java.io.File;
import java.net.URISyntaxException;

import org.tuml.generation.JavaGenerator;
import org.tuml.javageneration.DefaultVisitors;
import org.tuml.javageneration.TumlLibVisitors;
import org.tuml.restlet.generation.RestletVisitors;

public class GenerateTestProjects {

	public static void main(String[] args) throws URISyntaxException {
        if (args.length == 0) {
            args = new String[]{"/home/pieter/workspace-tuml/tuml/"};
        }
		JavaGenerator javaGenerator = new JavaGenerator();
		javaGenerator.generate(new File(args[0] + "/test/tuml-test/src/main/model/tinker-test.uml"), new File(args[0] + "/test/tuml-test/"), DefaultVisitors.getDefaultJavaVisitors(), TumlLibVisitors.getDefaultJavaVisitors());
		javaGenerator = new JavaGenerator();
		javaGenerator.generate(new File(args[0] + "/test/test-restlet/src/main/model/restAndJson.uml"), new File(args[0] + "/test/test-restlet/"), RestletVisitors.getDefaultJavaVisitors(), TumlLibVisitors.getDefaultJavaVisitors());
		javaGenerator = new JavaGenerator();
		javaGenerator.generate(new File(args[0] + "/test/tuml-test-basic/src/main/model/tinker-test-basic.uml"), new File(args[0] + "/test/tuml-test-basic/"), DefaultVisitors.getDefaultJavaVisitors(), TumlLibVisitors.getDefaultJavaVisitors());
		javaGenerator = new JavaGenerator();
		javaGenerator.generate(new File(args[0] + "/test/tuml-test-ocl/src/main/model/test-ocl.uml"), new File(args[0] + "/test/tuml-test-ocl/"), DefaultVisitors.getDefaultJavaVisitors());
        javaGenerator = new JavaGenerator();
        javaGenerator.generate(new File(args[0] + "/test/test-restlet-minimal/src/main/model/test-restlet-minimal.uml"), new File(args[0] + "/test/test-restlet-minimal/"), RestletVisitors.getDefaultJavaVisitors());
    }
}
