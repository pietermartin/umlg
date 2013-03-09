package org.tuml.testbasic;

import org.tuml.framework.Visitor;
import org.tuml.generation.JavaGenerator;
import org.tuml.javageneration.DefaultVisitors;

import java.io.File;
import java.util.List;

public class GenerateTestBasic {

	public static void main(String[] args) {
		JavaGenerator javaGenerator = new JavaGenerator();
        List<Visitor<?>> visitors = DefaultVisitors.getDefaultJavaVisitors();
		javaGenerator.generate(new File("src/main/model/tinker-test-basic.uml"), new File("./"),
                visitors);
	}

}
