package org.tuml.framework;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

public class ModelPrinter {

	public static Map<String, String> sourceFiles = new HashMap<String, String>();
	private final static String SOURCE_FOLDER = "src/main/generated-java";
	
	public static void addToSource(String name, String source) {
		sourceFiles.put(name, source);
	}

	public static void toText() {
		File sourceDir = new File(SOURCE_FOLDER);
		try {
			FileUtils.cleanDirectory(sourceDir);
			for (String name : sourceFiles.keySet()) {
				String packageName = name.substring(0, name.lastIndexOf(".")).replace(".", "/");
				File javaPackage = new File(SOURCE_FOLDER + "/" + packageName);
				javaPackage.mkdirs();
				String javaFileName = name.substring(name.lastIndexOf(".") + 1);
				BufferedWriter writer = new BufferedWriter(new FileWriter(new File(SOURCE_FOLDER + "/" + packageName + "/" + javaFileName + ".java")));
				writer.append(sourceFiles.get(name));
				writer.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
