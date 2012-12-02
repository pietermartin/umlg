package org.tuml.restAndjson;

import java.io.File;

import org.tuml.generation.JavaGenerator;
import org.tuml.restlet.generation.RestletVisitors;

public class GenerateRestAndJsonTest {
	public static void main(String[] args) {
		JavaGenerator javaGenerator = new JavaGenerator();
		javaGenerator
				.generate(new File("test/test-restlet/src/main/model/restANDjson.uml"), new File("./test/test-restlet"), RestletVisitors.getDefaultJavaVisitors());
	}
}
