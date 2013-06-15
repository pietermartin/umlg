package org.umlg.javageneration.util;

import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.umlg.javageneration.validation.Date;
import org.umlg.javageneration.validation.DateTime;
import org.umlg.javageneration.validation.Email;
import org.umlg.javageneration.validation.Max;
import org.umlg.javageneration.validation.MaxLength;
import org.umlg.javageneration.validation.Min;
import org.umlg.javageneration.validation.MinLength;
import org.umlg.javageneration.validation.Range;
import org.umlg.javageneration.validation.RangeLength;
import org.umlg.javageneration.validation.Time;
import org.umlg.javageneration.validation.Url;
import org.umlg.javageneration.validation.Validation;

public enum TumlValidationEnum {
    MinLength("validateMinLength", new String[]{"length"}), MaxLength("validateMaxLength", new String[]{"length"}), RangeLength("validateRangeLength",
            new String[]{"min", "max"}), Min("validateMin", new String[]{"value"}), Max("validateMax", new String[]{"value"}), Range("validateRange",
            new String[]{"min", "max"}), URL("validateUrl", new String[]{"protocol", "host", "port", "regexp", "flags"}),
    Email("validateEmail", new String[]{}),
    DateTime("validateDateTime", new String[]{}),
    Date("validateDate", new String[]{}),
    Time("validateTime", new String[]{});
    private String methodName;
    private String[] attributes;

    private TumlValidationEnum(String methodName, String[] attributes) {
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
        } else if (stereotype.getName().equals(Max.name())) {
            return new Max((Integer) p.getValue(stereotype, "value"));
        } else if (stereotype.getName().equals(Min.name())) {
            return new Min((Integer) p.getValue(stereotype, "value"));
        } else if (stereotype.getName().equals(Range.name())) {
            return new Range((Integer) p.getValue(stereotype, "min"), (Integer) p.getValue(stereotype, "max"));
        } else if (stereotype.getName().equals(URL.name())) {
            return new Url((String) p.getValue(stereotype, "protocol"), (String) p.getValue(stereotype, "host"), (Integer) p.getValue(stereotype, "port"),
                    (String) p.getValue(stereotype, "regexp"), (String) p.getValue(stereotype, "flags"));

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
