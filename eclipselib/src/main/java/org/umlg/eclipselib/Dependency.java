package org.umlg.eclipselib;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

/**
 * Date: 2013/11/04
 * Time: 4:33 PM
 */
public class Dependency {

    private static Logger logger = Logger.getLogger(Dependency.class.getPackage().getName());
    private static String GPG_COMMAND = "gpg --batch --yes --passphrase-file /home/pieter/Documents/gpgpassphrase/passphrase -ab ";

    private String groupId;
    private String artifactId;
    private String version;

    public Dependency(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public void copyToProject(File m2, File eclipseLib) throws IOException, InterruptedException {
        logger.fine(String.format("start preparing artifacts for %s", toString()));
        String[] groupIdArray = this.groupId.split("\\.");
        File groupIdDir = m2;
        File eclipseLibGroupIdDir = eclipseLib;
        for (String path : groupIdArray) {
            groupIdDir = new File(groupIdDir, path);
            eclipseLibGroupIdDir = new File(eclipseLibGroupIdDir, path);
        }
        String[] artifactIdArray = this.artifactId.split("\\.");
        File artifactIdDir = groupIdDir;
        File eclipseLibArtifactIdDir = eclipseLibGroupIdDir;
        for (String path : artifactIdArray) {
            artifactIdDir = new File(artifactIdDir, path);
            eclipseLibArtifactIdDir = new File(eclipseLibArtifactIdDir, path);
        }
        File versionDir = new File(artifactIdDir, this.version);
        File eclipseLibVersionDir = new File(eclipseLibArtifactIdDir, this.version);
        if (!eclipseLibVersionDir.exists()) {
            eclipseLibVersionDir.mkdirs();
        }
        File jar = new File(versionDir, getMavenJarFileName());
        File pom = new File(versionDir, getMavenPomFileName());
        File sourceDir = new File(artifactIdDir, "source");
        File sourceVersionDir = new File(sourceDir, this.version);
        File source = new File(sourceVersionDir, getMavenSourceFileName());

        Path jarPath = Paths.get(jar.getAbsolutePath());
        Path pomPath = Paths.get(pom.getAbsolutePath());
        Path soursePath = Paths.get(source.getAbsolutePath());

        Path eclipseLibDestination = Paths.get(eclipseLibVersionDir.getAbsolutePath());

        File jarTarget = Files.copy(jarPath, eclipseLibDestination.resolve(jarPath.getFileName())).toFile();
        File pomTarget = Files.copy(pomPath, eclipseLibDestination.resolve(pomPath.getFileName())).toFile();

        //Edit the pom
        correctPomForUpload(pomTarget);

        File sourceTarget = Files.copy(soursePath, eclipseLibDestination.resolve(soursePath.getFileName())).toFile();
        File destSource = new File(sourceTarget.getParentFile(), jar.getName().replace(".jar", "-sources.jar"));
        sourceTarget.renameTo(destSource);

        //Run signing
        logger.fine(String.format("signing artifacts for %s", toString()));
        Runtime.getRuntime().exec(GPG_COMMAND + jarTarget.getName(), null, jarTarget.getParentFile()).waitFor();
        Runtime.getRuntime().exec(GPG_COMMAND + pomTarget.getName(), null, pomTarget.getParentFile()).waitFor();
        Runtime.getRuntime().exec(GPG_COMMAND + destSource.getName(), null, destSource.getParentFile()).waitFor();

        //Create java doc
        File unjarredSourceDir = new File(destSource.getParentFile(), "source");
        unjarredSourceDir.mkdir();
        File unjarredJavadocDir = new File(destSource.getParentFile(), "javadoc");
        unjarredJavadocDir.mkdir();
        FileUtils.copyFileToDirectory(destSource, unjarredSourceDir);
        Runtime.getRuntime().exec("jar -xf " + destSource.getName(), null, unjarredSourceDir).waitFor();
        //delete the copied file
        File unjarredSourceFileCopied = new File(unjarredSourceDir, destSource.getName());
        unjarredSourceFileCopied.delete();

        //Create java doc
        logger.fine(String.format("generating javadoc artifacts for %s", toString()));
        Runtime.getRuntime().exec("javadoc -d ./javadoc/ -sourcepath ./source/ ", null, destSource.getParentFile()).waitFor();
        logger.fine(String.format("generating javadoc jar artifacts for %s", toString()));
        Runtime.getRuntime().exec("jar -cvf " + destSource.getName().replace("-sources.jar", "-javadoc.jar *"), null, unjarredJavadocDir).waitFor();
        Runtime.getRuntime().exec(GPG_COMMAND + destSource.getName(), null, destSource.getParentFile()).waitFor();

        File newJavaDocJar = new File(unjarredJavadocDir, destSource.getName().replace("-sources.jar", "-javadoc.jar"));
        FileUtils.copyFileToDirectory(newJavaDocJar, destSource.getParentFile());
        newJavaDocJar = new File(destSource.getParentFile(), newJavaDocJar.getName());
        Runtime.getRuntime().exec(GPG_COMMAND + newJavaDocJar.getName(), null, newJavaDocJar.getParentFile()).waitFor();
        FileUtils.forceDelete(unjarredSourceDir);
        FileUtils.forceDelete(unjarredJavadocDir);

        //Bundle the whole lot
        logger.fine(String.format("creating bundle %s", toString()));
        Runtime.getRuntime().exec("jar -cvf  " + this.artifactId + "-bundle.jar " +
                jarTarget.getName() + " " +
                jarTarget.getName().replace(".jar", ".jar.asc") + " " +
                pomTarget.getName() + " " +
                pomTarget.getName().replace(".pom", ".pom.asc") + " " +
                destSource.getName() + " " +
                destSource.getName().replace(".jar", ".jar.asc") + " " +
                newJavaDocJar.getName() + " " +
                newJavaDocJar.getName().replace(".jar", ".jar.asc"),
                null,
                newJavaDocJar.getParentFile()
        ).waitFor();
        logger.fine(String.format("done with %s", toString()));
    }

    private void correctPomForUpload(File pomTarget) throws IOException {
        List<String> pomAsList = FileUtils.readLines(pomTarget);

        //Find the name
        int nameLine = 0;
        String description = "";
        for (String line : pomAsList) {
            nameLine++;
            if (line.contains("<name>")) {
                String tmp = line.trim();
                tmp = tmp.substring(6);
                description = tmp.replace("</name>", "");
                break;
            }

        }

        StringBuilder sb = new StringBuilder();
        sb.append("  <description>");
        sb.append(description);
        sb.append("</description>\n");
        sb.append("  <packaging>jar</packaging>\n");
        sb.append("  <url>http://www.eclipse.org</url>\n");
        sb.append("  <scm>\n");
        sb.append("    <url/>\n");
        sb.append("    <connection/>\n");
        sb.append("  </scm>\n");
        sb.append("  <developers>\n");
        sb.append("    <developer/>\n");
        sb.append("  </developers>");
        pomAsList.add(nameLine, sb.toString());

        //Check if there is a licence part, if not give it an eclipse licence
        boolean hasLicence = false;
        int endOfdevelopers = 0;
        nameLine = 0;
        for (String s : pomAsList) {
            nameLine++;
            if (s.contains("</developers>")) {
                endOfdevelopers = nameLine;
            }
            if (s.contains("<licenses>")) {
                hasLicence = true;
                break;
            }
        }
        if (!hasLicence) {
            sb.setLength(0);
            sb.append("  <licenses>\n");
            sb.append("    <license>\n");
            sb.append("      <name>Eclipse Public License - v 1.0</name>\n");
            sb.append("      <url>http://www.eclipse.org/org/documents/epl-v10.html</url>\n");
            sb.append("    </license>\n");
            sb.append("  </licenses>");
            pomAsList.add(endOfdevelopers, sb.toString());
        }

        FileUtils.writeLines(pomTarget, pomAsList);
    }

    private String getMavenJarFileName() {
        return this.artifactId + "-" + this.version + ".jar";
    }

    private String getMavenPomFileName() {
        return this.artifactId + "-" + this.version + ".pom";
    }

    private String getMavenSourceFileName() {
        return "source-" + this.version + ".jar";
    }


    public String toString() {
        return this.groupId + ":" + this.artifactId + ":" + this.version;
    }
}
