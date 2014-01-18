package org.umlg.restlet.client.json;


import org.umlg.restlet.client.json.validation.UmlgValidation;
import org.umlg.restlet.client.json.validation.ValidationEnum;

/**
 * Date: 2013/02/02
 * Time: 7:25 PM
 */
public class ValidationJson {

    private String name;
    private UmlgValidation umlgValidation;

    public ValidationJson(String validationName, Object o) {
        ValidationEnum validationEnum = ValidationEnum.fromName(validationName);
        this.umlgValidation = validationEnum.getValidation(o);
        this.name = validationName;
    }

    public String getName() {
        return name;
    }

    public UmlgValidation getUmlgValidation() {
        return umlgValidation;
    }
}
