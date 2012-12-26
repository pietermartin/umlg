package org.tuml.java.metamodel.annotation;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.tuml.java.metamodel.OJParameter;
import org.tuml.java.metamodel.OJPathName;
import org.tuml.java.metamodel.utilities.JavaStringHelpers;
import org.tuml.java.metamodel.utilities.JavaUtil;


public class OJAnnotatedParameter extends OJParameter implements OJAnnotatedElement {
	Map<OJPathName, OJAnnotationValue> f_annotations = new TreeMap<OJPathName, OJAnnotationValue>();
	public OJAnnotatedParameter(String string, OJPathName ojPathName) {
		this.setName(string);
		this.setType(ojPathName);
	}
	public OJAnnotatedParameter() {
	}

	public Collection<OJAnnotationValue> getAnnotations() {
		return f_annotations.values();
	}

	public boolean addAnnotationIfNew(OJAnnotationValue value){
		if(f_annotations.containsKey(value.getType())){
			return false;
		}else{
			putAnnotation(value);
			return true;
		}
	}

	public OJAnnotationValue putAnnotation(OJAnnotationValue value) {
		return f_annotations.put(value.getType(), value);
	}

	public OJAnnotationValue removeAnnotation(OJPathName type) {
		return f_annotations.remove(type);
	}

	@Override
	public String toJavaString() {
		StringBuilder sb = new StringBuilder();
		if (!getComment().equals("")) {
			sb.append("\t// ");
			sb.append(getComment());
			sb.append("\n");
		}
		if (isFinal) {
			sb.append("final ");
		}		
		if (getAnnotations().size() > 0) {
			sb.append(JavaStringHelpers.indent(JavaUtil.collectionToJavaString(getAnnotations(), " "), 0));
		}
		if (sb.length() > 0) {
			sb.append(' ');
		}
		sb.append(getType().getCollectionTypeName());
		sb.append(' ');
		sb.append(getName());
		return sb.toString();
	}

	public OJAnnotatedParameter getDeepCopy() {
		OJAnnotatedParameter copy = new OJAnnotatedParameter();
		copyDeepInfoInto(copy);
		return copy;
	}

	public void copyDeepInfoInto(OJAnnotatedParameter copy) {
		super.copyDeepInfoInto(copy);
		Collection<OJAnnotationValue> annotations = getAnnotations();
		for (OJAnnotationValue ojAnnotationValue : annotations) {
			OJAnnotationValue copyAnnotation = ojAnnotationValue.getDeepCopy();
			copy.addAnnotationIfNew(copyAnnotation);
		}
	}

	public void renameAll(Set<OJPathName> renamePathNames, String newName) {
		super.renameAll(renamePathNames, newName);
		Collection<OJAnnotationValue> annotations = getAnnotations();
		for (OJAnnotationValue ojAnnotationValue : annotations) {
			Set<OJPathName> usedTypes = ojAnnotationValue.getAllTypesUsed();
			for (OJPathName usedType : usedTypes) {
				usedType.renameAll(renamePathNames, newName);
			}
		}
	}

	public OJAnnotationValue findAnnotation(OJPathName path) {
		return AnnotationHelper.getAnnotation(this, path);
	}
}
