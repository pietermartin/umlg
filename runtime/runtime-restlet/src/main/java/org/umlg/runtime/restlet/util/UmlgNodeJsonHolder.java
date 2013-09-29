package org.umlg.runtime.restlet.util;

import org.apache.commons.lang.StringUtils;
import org.umlg.runtime.domain.AssociationClassNode;
import org.umlg.runtime.domain.UmlgNode;

/**
 * Date: 2013/09/29
 * Time: 10:10 AM
 */
public class UmlgNodeJsonHolder {

    private UmlgNode umlgNode;
    private String associationClassPropertyName;
    private AssociationClassNode associationClassNode;

    public UmlgNodeJsonHolder(UmlgNode umlgNode) {
        this.umlgNode = umlgNode;
    }

    public UmlgNodeJsonHolder(UmlgNode umlgNode, String associationClassPropertyName, AssociationClassNode associationClassNode) {
        this.umlgNode = umlgNode;
        this.associationClassPropertyName = associationClassPropertyName;
        this.associationClassNode = associationClassNode;
    }

    public String toJson() {
        StringBuilder jsonResult = new StringBuilder();
        jsonResult.append(umlgNode.toJsonWithoutCompositeParent(true));
        if (!StringUtils.isEmpty(this.associationClassPropertyName)) {
            jsonResult.delete(jsonResult.length() - 1, jsonResult.length());
            jsonResult.append(", \"" + this.associationClassPropertyName + "\": ");
            jsonResult.append(this.associationClassNode.toJsonWithoutCompositeParent(true));
            jsonResult.append("}");
        }
        return jsonResult.toString();
    }
}
