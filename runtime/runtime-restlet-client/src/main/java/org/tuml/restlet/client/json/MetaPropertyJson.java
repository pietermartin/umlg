package org.tuml.restlet.client.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Date: 2013/02/02
 * Time: 2:56 PM
 */
public class MetaPropertyJson {

    private String name;
    private Boolean onePrimitive;
    private Boolean readOnly;
    private String dateTypeEnum;
    private List<ValidationJson> validations = new ArrayList<ValidationJson>();
    private String qualifiedName;
    private String inverseQualifiedName;
    private Boolean manyPrimitive;
    private Boolean oneEnumeration;
    private Boolean manyEnumeration;
    private Boolean controllingSide;
    private Boolean composite;
    private Boolean inverseComposite;
    private Boolean oneToOne;
    private Boolean oneToMany;
    private Boolean manyToOne;
    private Boolean manyToMany;
    private Integer upper;
    private Integer lower;
    private String label;
    private Boolean qualified;
    private Boolean inverseQualified;
    private Boolean ordered;
    private Boolean inverseOrdered;
    private Boolean unique;
    private Boolean inverseUnique;
    private String tumlUri;
    private String tumlOverloadedPostUri;
    private String tumlMetaDataUri;
    private String fieldType;
    private String tumlLookupUri;
    private String tumlLookupOnCompositeParentUri;
    private String tumlCompositeParentLookupUri;
    private String tumlCompositeParentLookupUriOnCompositeParent;

    public MetaPropertyJson(Map<String, Object> properties) {
        for (String name : properties.keySet()) {
            if (name.equals("name")) {
                this.name = (String) properties.get(name);
            } else if (name.equals("onePrimitive")) {
                this.onePrimitive = (Boolean) properties.get(name);
            } else if (name.equals("readOnly")) {
                this.readOnly = (Boolean) properties.get(name);
            } else if (name.equals("dataTypeEnum")) {
                this.dateTypeEnum = (String) properties.get(name);
            } else if (name.equals("validations")) {
                Map<String, Object> validations = (Map<String, Object>) properties.get(name);
                if (validations != null) {
                    for (String validationName : validations.keySet()) {
                        this.validations.add(new ValidationJson(validationName, validations.get(validationName)));
                    }
                }
            } else if (name.equals("qualifiedName")) {
                this.qualifiedName = (String) properties.get(name);
            } else if (name.equals("inverseQualifiedName")) {
                this.inverseQualifiedName = (String) properties.get(name);
            } else if (name.equals("manyPrimitive")) {
                this.manyPrimitive = (Boolean) properties.get(name);
            } else if (name.equals("oneEnumeration")) {
                this.oneEnumeration = (Boolean) properties.get(name);
            } else if (name.equals("manyEnumeration")) {
                this.manyEnumeration = (Boolean) properties.get(name);
            } else if (name.equals("controllingSide")) {
                this.controllingSide = (Boolean) properties.get(name);
            } else if (name.equals("composite")) {
                this.composite = (Boolean) properties.get(name);
            } else if (name.equals("inverseComposite")) {
                this.inverseComposite = (Boolean) properties.get(name);
            } else if (name.equals("oneToOne")) {
                this.oneToOne = (Boolean) properties.get(name);
            } else if (name.equals("oneToMany")) {
                this.oneToMany = (Boolean) properties.get(name);
            } else if (name.equals("manyToOne")) {
                this.manyToOne = (Boolean) properties.get(name);
            } else if (name.equals("manyToMany")) {
                this.manyToMany = (Boolean) properties.get(name);
            } else if (name.equals("upper")) {
                this.upper = (Integer) properties.get(name);
            } else if (name.equals("lower")) {
                this.lower = (Integer) properties.get(name);
            } else if (name.equals("label")) {
                this.label = (String) properties.get(name);
            } else if (name.equals("qualified")) {
                this.qualified = (Boolean) properties.get(name);
            } else if (name.equals("inverseQualified")) {
                this.inverseQualified = (Boolean) properties.get(name);
            } else if (name.equals("ordered")) {
                this.ordered = (Boolean) properties.get(name);
            } else if (name.equals("inverseOrdered")) {
                this.inverseOrdered = (Boolean) properties.get(name);
            } else if (name.equals("unique")) {
                this.unique = (Boolean) properties.get(name);
            } else if (name.equals("inverseUnique")) {
                this.inverseUnique = (Boolean) properties.get(name);
            } else if (name.equals("tumlUri")) {
                this.tumlUri = (String) properties.get(name);
            } else if (name.equals("tumlOverloadedPostUri")) {
                this.tumlOverloadedPostUri = (String) properties.get(name);
            } else if (name.equals("tumlMetaDataUri")) {
                this.tumlMetaDataUri = (String) properties.get(name);
            } else if (name.equals("fieldType")) {
                this.fieldType = (String) properties.get(name);
            } else if (name.equals("tumlLookupUri")) {
                this.tumlLookupUri = (String) properties.get(name);
            } else if (name.equals("tumlLookupOnCompositeParentUri")) {
                this.tumlLookupOnCompositeParentUri = (String) properties.get(name);
            } else if (name.equals("tumlCompositeParentLookupUri")) {
                this.tumlCompositeParentLookupUri = (String) properties.get(name);
            } else if (name.equals("tumlCompositeParentLookupUriOnCompositeParent")) {
                this.tumlCompositeParentLookupUriOnCompositeParent = (String) properties.get(name);
            } else {
                throw new IllegalStateException(String.format("Meta property %s not catered for", new Object[]{name}));
            }
        }
    }

    public String getName() {
        return name;
    }

    public Boolean getOnePrimitive() {
        return onePrimitive;
    }

    public Boolean getReadOnly() {
        return readOnly;
    }

    public String getDateTypeEnum() {
        return dateTypeEnum;
    }

    public List<ValidationJson> getValidations() {
        return validations;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public String getInverseQualifiedName() {
        return inverseQualifiedName;
    }

    public Boolean getManyPrimitive() {
        return manyPrimitive;
    }

    public Boolean getOneEnumeration() {
        return oneEnumeration;
    }

    public Boolean getManyEnumeration() {
        return manyEnumeration;
    }

    public Boolean getControllingSide() {
        return controllingSide;
    }

    public Boolean getComposite() {
        return composite;
    }

    public Boolean getInverseComposite() {
        return inverseComposite;
    }

    public Boolean getOneToOne() {
        return oneToOne;
    }

    public Boolean getOneToMany() {
        return oneToMany;
    }

    public Boolean getManyToOne() {
        return manyToOne;
    }

    public Boolean getManyToMany() {
        return manyToMany;
    }

    public Integer getUpper() {
        return upper;
    }

    public Integer getLower() {
        return lower;
    }

    public String getLabel() {
        return label;
    }

    public Boolean getQualified() {
        return qualified;
    }

    public Boolean getInverseQualified() {
        return inverseQualified;
    }

    public Boolean getOrdered() {
        return ordered;
    }

    public Boolean getInverseOrdered() {
        return inverseOrdered;
    }

    public Boolean getUnique() {
        return unique;
    }

    public Boolean getInverseUnique() {
        return inverseUnique;
    }

    public String getTumlUri() {
        return tumlUri;
    }

    public String getTumlOverloadedPostUri() {
        return tumlOverloadedPostUri;
    }

    public String getTumlMetaDataUri() {
        return tumlMetaDataUri;
    }

    public String getFieldType() {
        return fieldType;
    }

    public String getTumlLookupUri() {
        return tumlLookupUri;
    }

    public String getTumlLookupOnCompositeParentUri() {
        return tumlLookupOnCompositeParentUri;
    }

    public String getTumlCompositeParentLookupUri() {
        return tumlCompositeParentLookupUri;
    }

    public String getTumlCompositeParentLookupUriOnCompositeParent() {
        return tumlCompositeParentLookupUriOnCompositeParent;
    }

}
