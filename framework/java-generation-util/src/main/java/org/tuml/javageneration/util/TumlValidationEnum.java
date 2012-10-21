package org.tuml.javageneration.util;

import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.tuml.javageneration.validation.Max;
import org.tuml.javageneration.validation.MaxLength;
import org.tuml.javageneration.validation.Min;
import org.tuml.javageneration.validation.MinLength;
import org.tuml.javageneration.validation.Range;
import org.tuml.javageneration.validation.RangeLength;
import org.tuml.javageneration.validation.Url;
import org.tuml.javageneration.validation.Validation;

public enum TumlValidationEnum {
	MinLength("validateMinLength", new String[] { "length" }), MaxLength("validateMaxLength", new String[] { "length" }), RangeLength("validateRangeLength", new String[] { "min",
			"max" }), Min("validateMin", new String[] { "value" }), Max("validateMax", new String[] { "value" }), Range("validateRange", new String[] { "min", "max" }), URL(
			"validateUrl", new String[] { "protocol", "host", "port", "regexp", "flags" });
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
			return new Url((String) p.getValue(stereotype, "protocol"), (String) p.getValue(stereotype, "host"), (Integer) p.getValue(stereotype, "port"), (String) p.getValue(
					stereotype, "regexp"), (String) p.getValue(stereotype, "flags"));

		} else {
			throw new IllegalStateException("Unknown validation stereotype " + stereotype.getName());
		}
	}

}
