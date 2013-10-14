package org.umlg.framework;

import org.eclipse.uml2.uml.*;

import java.util.List;

/**
 * Filters out DataType properties.
 * I.e. is there is an association from a DataType to a Classifier then the DataType's property is filtered out.
 * Date: 2013/10/13
 * Time: 8:59 PM
 */
public class DataTypePropertyFilter implements ElementFilter {
    @Override
    public boolean filter(Element e) {
        if (!(e instanceof Property)) {
            return false;
        } else {
            Property p = (Property)e;
            Element owner = p.getOwner();

            //Check if it is a qualifier
            if (owner instanceof Property && ((Property) owner).getQualifiers().contains(p)) {
                return false;
            } else {

                //Check if it is an association class
                if (owner instanceof AssociationClass && ((AssociationClass)owner).getOwnedAttributes().contains(p)) {
                    return false;
                } else if (owner instanceof Association) {
                    Association a = (Association) owner;
                    List<Property> members = a.getMemberEnds();
                    Property otherEnd = null;
                    for (Property member : members) {
                        if (member != p) {
                            otherEnd = member;
                            break;
                        }
                    }
                    if (otherEnd == null) {
                        throw new IllegalStateException("Oy, where is the other end gone to!!!");
                    }
                    return otherEnd.getType() instanceof DataType;
                } else  {
                    return false;
                }
            }
        }
    }
}
