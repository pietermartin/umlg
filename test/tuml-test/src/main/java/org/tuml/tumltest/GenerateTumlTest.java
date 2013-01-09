package org.tuml.tumltest;

import org.tuml.framework.Visitor;
import org.tuml.generation.JavaGenerator;
import org.tuml.javageneration.DefaultVisitors;
import org.tuml.javageneration.TumlLibVisitors;

import java.io.File;
import java.util.List;

public class GenerateTumlTest {

	public static void main(String[] args) {
		JavaGenerator javaGenerator = new JavaGenerator();
        List<Visitor<?>> visitors = DefaultVisitors.getDefaultJavaVisitors();
        visitors.addAll(TumlLibVisitors.getDefaultJavaVisitors());
		javaGenerator.generate(
                new File("test/tuml-test/src/main/model/tinker-test.uml"),
                new File("./test/tuml-test"),
                visitors);
	}

}
