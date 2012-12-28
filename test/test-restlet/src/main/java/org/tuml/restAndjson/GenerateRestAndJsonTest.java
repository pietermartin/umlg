package org.tuml.restAndjson;

import java.io.File;
import java.util.List;

import org.tuml.framework.Visitor;
import org.tuml.generation.JavaGenerator;
import org.tuml.javageneration.DefaultVisitors;
import org.tuml.javageneration.TumlLibVisitors;
import org.tuml.restlet.generation.RestletVisitors;

public class GenerateRestAndJsonTest {
	public static void main(String[] args) {
		JavaGenerator javaGenerator = new JavaGenerator();
		javaGenerator
				.generate(new File("test/test-restlet/src/main/model/restANDjson.uml"), new File("./test/test-restlet"), RestletVisitors.getDefaultJavaVisitors());
	}
}
