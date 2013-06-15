package org.umlg.restlet.client.json;


import org.umlg.restlet.client.json.validation.TumlValidation;
import org.umlg.restlet.client.json.validation.ValidationEnum;

/**
 * Date: 2013/02/02
 * Time: 7:25 PM
 */
public class ValidationJson {

    private String name;
    private TumlValidation tumlValidation;

    public ValidationJson(String validationName, Object o) {
        ValidationEnum validationEnum = ValidationEnum.fromName(validationName);
        this.tumlValidation = validationEnum.getValidation(o);
        this.name = validationName;
    }

    public String getName() {
        return name;
    }

    public TumlValidation getTumlValidation() {
        return tumlValidation;
    }
}
