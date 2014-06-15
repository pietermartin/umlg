package org.umlg.runtime.adaptor;

import org.apache.commons.io.FileUtils;
import org.umlg.runtime.util.UmlgProperties;
import org.umlg.runtime.util.UmlgUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Date: 2012/12/29
 * Time: 9:23 PM
 */
public class UmlgGraphManager {

    public static UmlgGraphManager INSTANCE = new UmlgGraphManager();
    private static final Logger logger = Logger.getLogger(UmlgGraphManager.class.getPackage().getName());
    private UmlgGraphFactory nakedGraphFactory;

    private UmlgGraphManager() {

    }

    public UmlgGraph startupGraph() {
        try {
            String dbUrl = UmlgProperties.INSTANCE.getUmlgDbLocation();
            if (this.nakedGraphFactory == null) {
                UmlgAdaptorImplementation umlgAdaptorImplementation = UmlgAdaptorImplementation.fromName(UmlgUtil.getBlueprintsImplementation());
                @SuppressWarnings("unchecked")
                Class<UmlgGraphFactory> factory = (Class<UmlgGraphFactory>) Class.forName(umlgAdaptorImplementation.getTumlGraphFactory());
                Method m = factory.getDeclaredMethod("getInstance", new Class[0]);
                this.nakedGraphFactory = (UmlgGraphFactory) m.invoke(null);
            }
            UmlgGraph umlgGraph = nakedGraphFactory.getTumlGraph(dbUrl);
            return umlgGraph;
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException)e;
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Delete graph invokes drop on the db and then deletes the files.
     */
    public void deleteGraph() {
        try {
            if (this.nakedGraphFactory == null) {
                UmlgAdaptorImplementation umlgAdaptorImplementation = UmlgAdaptorImplementation.fromName(UmlgUtil.getBlueprintsImplementation());
                @SuppressWarnings("unchecked")
                Class<UmlgGraphFactory> factory = (Class<UmlgGraphFactory>) Class.forName(umlgAdaptorImplementation.getTumlGraphFactory());
                Method m = factory.getDeclaredMethod("getInstance", new Class[0]);
                this.nakedGraphFactory = (UmlgGraphFactory) m.invoke(null);
            }
            nakedGraphFactory.shutdown();
            nakedGraphFactory.clear();
            //Delete the files
            String dbUrl = UmlgProperties.INSTANCE.getUmlgDbLocation();
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
            //Remove threadvars
            UMLG.remove();
            TransactionThreadVar.remove();
            TransactionThreadEntityVar.remove();
            TransactionThreadMetaNodeVar.remove();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void backupGraph() {
        try {
            String dbUrl = UmlgProperties.INSTANCE.getUmlgDbLocation();
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


}
