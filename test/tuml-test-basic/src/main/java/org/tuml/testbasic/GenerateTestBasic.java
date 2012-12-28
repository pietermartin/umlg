package org.tuml.testbasic;

import java.io.File;
import java.util.List;

import org.tuml.framework.Visitor;
import org.tuml.generation.JavaGenerator;
import org.tuml.javageneration.DefaultVisitors;
import org.tuml.javageneration.TumlLibVisitors;

public class GenerateTestBasic {

	public static void main(String[] args) {
		JavaGenerator javaGenerator = new JavaGenerator();
        List<Visitor<?>> visitors = DefaultVisitors.getDefaultJavaVisitors();
        visitors.addAll(TumlLibVisitors.getDefaultJavaVisitors());
		javaGenerator.generate(new File("src/main/model/tinker-test-basic.uml"), new File("./"),
                visitors);
	}

}
