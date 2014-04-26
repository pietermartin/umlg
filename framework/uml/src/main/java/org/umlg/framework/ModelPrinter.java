package org.umlg.framework;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.apache.commons.io.FileUtils;

public class ModelPrinter {

	private Map<Source, String> sourceFiles = new HashMap<Source, String>();
    private Map<Source, Properties> propertyFiles = new HashMap<Source, Properties>();

    public ModelPrinter(SOURCE_TYPE sourceType) {
        this.sourceType = sourceType;
    }

    private SOURCE_TYPE sourceType;


	public void addToSource(String qualifiedName, String sourceDir, String source) {
		sourceFiles.put(new ModelPrinter.Source(qualifiedName, sourceDir), source);
	}

    public void addToSource(String qualifiedName, String sourceDir, Properties properties) {
        propertyFiles.put(new ModelPrinter.Source(qualifiedName, sourceDir), properties);
    }

    public void clear() {
		this.sourceFiles.clear();
	}

	public void toText(File project) {
		if (project.exists()) {
			try {
				List<String> alreadyCleaned = new ArrayList<String>();
				// First clean all source directories
				for (Source source : sourceFiles.keySet()) {
					if (!alreadyCleaned.contains(source.sourceDir)) {
						alreadyCleaned.add(source.sourceDir);
						File sourceDir = new File(project, source.sourceDir);
						ModelPrinter.cleanDirectory(sourceDir);
					}
				}

				for (Source source : sourceFiles.keySet()) {
					String packageName = source.qualifiedName.substring(0, source.qualifiedName.lastIndexOf(".")).replace(".", "/");
					File javaPackage = new File(project, source.sourceDir + "/" + packageName);
					javaPackage.mkdirs();
					String javaFileName = source.qualifiedName.substring(source.qualifiedName.lastIndexOf(".") + 1);
                    File sourceFile;
                    switch (sourceType) {
                        case JAVA:
                            sourceFile =new File(project, source.sourceDir + "/" + packageName + "/" + javaFileName + ".java");
                            BufferedWriter writer = new BufferedWriter(new FileWriter(sourceFile));
                            writer.append(sourceFiles.get(source));
                            writer.close();
                            break;
                        case GROOVY:
                            sourceFile =new File(project, source.sourceDir + "/" + packageName + "/" + javaFileName + ".groovy");
                            writer = new BufferedWriter(new FileWriter(sourceFile));
                            writer.append(sourceFiles.get(source));
                            writer.close();
                            break;
                        default:
                            throw new IllegalStateException("Unknown sourceType " + sourceType);
                    }
				}

                for (Source source : propertyFiles.keySet()) {
                    File sourceFile;
                    switch (sourceType) {
                        case PROPERTIES:
                            sourceFile =new File(project, source.sourceDir + "/" + source.qualifiedName);
                            BufferedWriter writer = new BufferedWriter(new FileWriter(sourceFile));
                            propertyFiles.get(source).store(writer, "UMLG generated properties");
                            writer.close();
                            break;
                        default:
                            throw new IllegalStateException("Unknown sourceType " + sourceType);
                    }
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

    public enum SOURCE_TYPE {
        JAVA,GROOVY,PROPERTIES;
    }
}
