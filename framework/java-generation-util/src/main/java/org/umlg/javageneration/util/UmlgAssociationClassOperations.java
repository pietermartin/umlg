package org.umlg.javageneration.util;

import org.eclipse.uml2.uml.AssociationClass;

/**
 * Date: 2013/06/14
 * Time: 8:32 PM
 */
public class UmlgAssociationClassOperations extends UmlgClassOperations {


    /**
     * This returns true if the association class has no associations, no enums
     * @param associationClass
     */
    public static boolean isSimple(AssociationClass associationClass) {
          return true;
    }

}
