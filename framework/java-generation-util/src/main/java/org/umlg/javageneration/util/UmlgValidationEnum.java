package org.umlg.javageneration.util;

import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.umlg.javageneration.validation.*;

public enum UmlgValidationEnum {
    MinLength("validateMinLength", new String[]{"length"}),
    MaxLength("validateMaxLength", new String[]{"length"}),
    RangeLength("validateRangeLength", new String[]{"min", "max"}),
    MinInteger("validateMinInteger", new String[]{"value"}),
    MaxInteger("validateMaxInteger", new String[]{"value"}),
    RangeInteger("validateRangeInteger", new String[]{"min", "max"}),
    MinUnlimitedNatural("validateMinUnlimitedNatural", new String[]{"value"}),
    MaxUnlimitedNatural("validateMaxUnlimitedNatural", new String[]{"value"}),
    RangeUnlimitedNatural("validateRangeUnlimitedNatural", new String[]{"min", "max"}),
    MinReal("validateMinReal", new String[]{"value"}),
    MaxReal("validateMaxReal", new String[]{"value"}),
    RangeReal("validateRangeReal", new String[]{"min", "max"}),
    Email("validateEmail", new String[]{}),
    DateTime("validateDateTime", new String[]{}),
    Date("validateDate", new String[]{}),
    Time("validateTime", new String[]{});
    private String methodName;
    private String[] attributes;

    private UmlgValidationEnum(String methodName, String[] attributes) {
        this.methodName = methodName;
        this.attributes = attributes;
    }

    public String[] getAttributes() {
        return attributes;
    }

    public String getMethodName() {
        return methodName;
    }

    public static Validation fromStereotype(Stereotype stereotype, Property p) {
        if (stereotype.getName().equals(MinLength.name())) {
            return new MinLength((Integer) p.getValue(stereotype, "length"));
        } else if (stereotype.getName().equals(MaxLength.name())) {
            return new MaxLength((Integer) p.getValue(stereotype, "length"));
        } else if (stereotype.getName().equals(RangeLength.name())) {
            return new RangeLength((Integer) p.getValue(stereotype, "min"), (Integer) p.getValue(stereotype, "max"));
        } else if (stereotype.getName().equals(MaxInteger.name())) {
            return new MaxInteger((Number) p.getValue(stereotype, "value"));
        } else if (stereotype.getName().equals(MinInteger.name())) {
            return new MinInteger((Integer) p.getValue(stereotype, "value"));
        } else if (stereotype.getName().equals(RangeInteger.name())) {
            return new RangeInteger((Integer) p.getValue(stereotype, "min"), (Integer) p.getValue(stereotype, "max"));
        } else if (stereotype.getName().equals(MaxUnlimitedNatural.name())) {
            return new MaxUnlimitedNatural((Integer) p.getValue(stereotype, "value"));
        } else if (stereotype.getName().equals(MinUnlimitedNatural.name())) {
            return new MinUnlimitedNatural((Integer) p.getValue(stereotype, "value"));
        } else if (stereotype.getName().equals(RangeUnlimitedNatural.name())) {
            return new RangeUnlimitedNatural((Integer)p.getValue(stereotype, "min"), (Integer)p.getValue(stereotype, "max"));
        } else if (stereotype.getName().equals(MaxReal.name())) {
            return new MaxReal((Double) p.getValue(stereotype, "value"));
        } else if (stereotype.getName().equals(MinReal.name())) {
            return new MinReal((Double) p.getValue(stereotype, "value"));
        } else if (stereotype.getName().equals(RangeReal.name())) {
            return new RangeReal((Double) p.getValue(stereotype, "min"), (Double) p.getValue(stereotype, "max"));
        } else {
            throw new IllegalStateException("Unknown validation stereotype " + stereotype.getName());
        }
    }

    public static Validation fromDataType(DataType dt) {
        DataTypeEnum dataTypeEnum = DataTypeEnum.fromDataType(dt);
        if (dataTypeEnum != null) {
            switch (dataTypeEnum) {
                case Audio:
                    break;
                case Date:
                    return new Date();
                case DateTime:
                    return new DateTime();
                case Time:
                    return new Time();
                case Email:
                    return new Email();
                case Image:
                    break;
                case InternationalPhoneNumber:
                    break;
                case LocalPhoneNumber:
                    break;
                case Video:
                    break;
                default:
                    break;
            }
            return null;
        } else {
            return null;
        }
    }

}
