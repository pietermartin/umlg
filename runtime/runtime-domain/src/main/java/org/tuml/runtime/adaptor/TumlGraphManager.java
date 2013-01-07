package org.tuml.runtime.adaptor;

import org.apache.commons.io.FileUtils;
import org.tuml.runtime.util.TumlProperties;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Date: 2013/01/04
 * Time: 6:26 PM
 */
public class TumlGraphManager {

    public static TumlGraphManager INSTANCE = new TumlGraphManager();
    private static final Logger logger = Logger.getLogger(TumlGraphManager.class.getPackage().getName());

    private TumlGraphManager() {
    }

    public void backupGraph() {
        try {
            String dbUrl = TumlProperties.INSTANCE.getTumlDbLocation();
            String parsedUrl = dbUrl;
            if (dbUrl.startsWith("local:")) {
                parsedUrl = dbUrl.replace("local:", "");
            }
            File dir = new File(parsedUrl);
            if (dir.exists()) {
                try {
                    File backupDir = new File(dir.getParent(), dir.getName() + "-" + new SimpleDateFormat("ddMMyyyy_mmss").format(new Date()));
                    logger.info(String.format("Moving dir %s to %s", new Object[]{dir.getAbsolutePath(), backupDir.getAbsolutePath()}));
                    FileUtils.moveDirectory(dir, backupDir);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteGraph() {
        try {
            String dbUrl = TumlProperties.INSTANCE.getTumlDbLocation();
            String parsedUrl = dbUrl;
            if (dbUrl.startsWith("local:")) {
                parsedUrl = dbUrl.replace("local:", "");
            }
            File dir = new File(parsedUrl);
            if (dir.exists()) {
                try {
                    logger.info(String.format("Deleting dir %s", new Object[]{dir.getAbsolutePath()}));
                    FileUtils.deleteDirectory(dir);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}