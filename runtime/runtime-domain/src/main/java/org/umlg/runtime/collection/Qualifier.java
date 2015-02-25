package org.umlg.runtime.collection;

import org.umlg.runtime.domain.UmlgNode;
import org.umlg.runtime.util.UmlgFormatter;

public class Qualifier {
    private String key;
    private Object value;
    private Multiplicity multiplicity;
    private UmlgRuntimeProperty umlgRuntimeProperty;

    public Qualifier(String key, Object value, Multiplicity multiplicity, UmlgRuntimeProperty umlgRuntimeProperty) {
        super();
        this.key = key;
        this.value = value;
        this.multiplicity = multiplicity;
        this.umlgRuntimeProperty = umlgRuntimeProperty;
    }

    public Object getValue() {
        if (value instanceof UmlgNode) {
            return ((UmlgNode) value).getUid();
        } else if (value instanceof Enum) {
            return ((Enum<?>) value).name();
        } else if (this.umlgRuntimeProperty.getDataTypeEnum() != null && !this.umlgRuntimeProperty.isOnePrimitive()) {
            return UmlgFormatter.format(this.umlgRuntimeProperty.getDataTypeEnum(), this.value);
        } else {
            return value;
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Multiplicity getMultiplicity() {
        return multiplicity;
    }

    public void setMultiplicity(Multiplicity multiplicity) {
        this.multiplicity = multiplicity;
    }

    public UmlgRuntimeProperty getUmlgRuntimeProperty() {
        return umlgRuntimeProperty;
    }

    public boolean isOne() {
        return this.multiplicity.isOne();
    }

    public boolean isMany() {
        return this.multiplicity.isMany();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("key = ");
        sb.append(this.key);
        sb.append(" value = ");
        sb.append(this.value);
        sb.append(" Multiplicity = ");
        sb.append(this.multiplicity);
        return sb.toString();
    }

}
