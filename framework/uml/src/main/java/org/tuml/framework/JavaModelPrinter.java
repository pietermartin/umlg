package org.tuml.framework;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

public class JavaModelPrinter {

	public static Map<Source, String> sourceFiles = new HashMap<Source, String>();

	public static void addToSource(String qualifiedName, String sourceDir, String source) {
		sourceFiles.put(new JavaModelPrinter.Source(qualifiedName, sourceDir), source);
	}

//	public static void addToSource(String qualifiedName, String source) {
//		sourceFiles.put(new JavaModelPrinter.Source(qualifiedName, SOURCE_FOLDER), source);
//	}

	public static void toText(File project) {
		if (project.exists()) {
			try {
				List<String> alreadyCleaned = new ArrayList<String>();
				// First clean all source directories
				for (Source source : sourceFiles.keySet()) {
					if (!alreadyCleaned.contains(source.sourceDir)) {
						alreadyCleaned.add(source.sourceDir);
						File sourceDir = new File(project, source.sourceDir);
						JavaModelPrinter.cleanDirectory(sourceDir);
					}
				}

				for (Source source : sourceFiles.keySet()) {
					String packageName = source.qualifiedName.substring(0, source.qualifiedName.lastIndexOf(".")).replace(".", "/");
					File javaPackage = new File(source.sourceDir + "/" + packageName);
					javaPackage.mkdirs();
					String javaFileName = source.qualifiedName.substring(source.qualifiedName.lastIndexOf(".") + 1);
					BufferedWriter writer = new BufferedWriter(new FileWriter(new File(source.sourceDir + "/" + packageName + "/" + javaFileName + ".java")));
					writer.append(sourceFiles.get(source));
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
		if (files == null) { // null if security restricted
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

	public static class Source {
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((qualifiedName == null) ? 0 : qualifiedName.hashCode());
			result = prime * result + ((sourceDir == null) ? 0 : sourceDir.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Source other = (Source) obj;
			if (qualifiedName == null) {
				if (other.qualifiedName != null)
					return false;
			} else if (!qualifiedName.equals(other.qualifiedName))
				return false;
			if (sourceDir == null) {
				if (other.sourceDir != null)
					return false;
			} else if (!sourceDir.equals(other.sourceDir))
				return false;
			return true;
		}
		public Source(String qualifiedName, String sourceDir) {
			super();
			this.qualifiedName = qualifiedName;
			this.sourceDir = sourceDir;
		}

		public String qualifiedName;
		public String sourceDir;
	}

}
