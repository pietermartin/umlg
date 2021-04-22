package org.umlg.testbasic;

import org.umlg.framework.Visitor;
import org.umlg.generation.JavaGenerator;
import org.umlg.javageneration.DefaultVisitors;

import java.io.File;
import java.util.List;

public class GenerateTestBasic {

	public static void main(String[] args) {
		JavaGenerator javaGenerator = new JavaGenerator();
        List<Visitor<?>> visitors = DefaultVisitors.getDefaultJavaVisitors();
		javaGenerator.generate(new File("src/main/model/umlg-test-basic.uml"),
				new File("./"),
                visitors);
	}

}
