package org.tuml.framework;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

public class JavaModelPrinter {

	public static Map<String, String> sourceFiles = new HashMap<String, String>();
	public final static String SOURCE_FOLDER = "src/main/generated-java";

	public static void addToSource(String name, String source) {
		sourceFiles.put(name, source);
	}

	public static void toText(File project) {
		if (project.exists()) {
			File sourceDir = new File(project, SOURCE_FOLDER);
			try {
				cleanDirectory(sourceDir);
				for (String name : sourceFiles.keySet()) {
					String packageName = name.substring(0, name.lastIndexOf(".")).replace(".", "/");
					File javaPackage = new File(sourceDir + "/" + packageName);
					javaPackage.mkdirs();
					String javaFileName = name.substring(name.lastIndexOf(".") + 1);
					BufferedWriter writer = new BufferedWriter(new FileWriter(new File(sourceDir + "/" + packageName + "/" + javaFileName + ".java")));
					writer.append(sourceFiles.get(name));
					writer.close();
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else {
			throw new IllegalStateException(String.format("Project does not exist at %s", project.getAbsolutePath()));
		}
	}
	
    public static void cleanDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (!directory.isDirectory()) {
            String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }

        File[] files = directory.listFiles();
        if (files == null) {  // null if security restricted
            throw new IOException("Failed to list contents of " + directory);
        }

        IOException exception = null;
        for (File file : files) {
        	if (file.getName().equals(".gitignore")) {
        		continue;
        	}
            try {
                FileUtils.forceDelete(file);
            } catch (IOException ioe) {
                exception = ioe;
            }
        }

        if (null != exception) {
            throw exception;
        }
    }
    
}
