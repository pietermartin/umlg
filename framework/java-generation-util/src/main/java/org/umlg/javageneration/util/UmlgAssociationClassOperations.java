package org.umlg.javageneration.util;

import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Generalization;

/**
 * Date: 2013/06/14
 * Time: 8:32 PM
 */
public class UmlgAssociationClassOperations extends UmlgClassOperations {


    /**
     * This returns true if the association class has no associations, no enums
     *
     * @param associationClass
     */
    public static boolean isSimple(AssociationClass associationClass) {
        return true;
    }

    public static boolean hasAssociationClassAsGeneralization(AssociationClass associationClass) {
        for (Generalization generalization : associationClass.getGeneralizations()) {
            if (generalization.getGeneral() instanceof AssociationClass) {
                return true;
            }
        }
        return false;
    }

    public static boolean extendsAssociationClass(Classifier classifier) {
        if (classifier instanceof AssociationClass) {
            return true;
        }
        for (Generalization generalization : classifier.getGeneralizations()) {
            if (generalization.getGeneral() instanceof AssociationClass) {
                return true;
            }
        }
        return false;
    }

}
