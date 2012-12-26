package org.tuml.java.metamodel.annotation;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.tuml.java.metamodel.OJElement;
import org.tuml.java.metamodel.OJPackage;
import org.tuml.java.metamodel.OJPathName;
import org.tuml.java.metamodel.utilities.JavaStringHelpers;
import org.tuml.java.metamodel.utilities.JavaUtil;

public class OJAnnotatedPackageInfo extends OJElement implements OJAnnotatedElement {
	private OJPackage myPackage;
	Map<OJPathName, OJAnnotationValue> f_annotations = new TreeMap<OJPathName, OJAnnotationValue>();
	public OJAnnotatedPackageInfo(String string) {
		setName(string);
	}
	public OJAnnotatedPackageInfo() {
		setName("");
	}

	public boolean addAnnotationIfNew(OJAnnotationValue value){
		if(f_annotations.containsKey(value.getType())){
			return false;
		}else{
			putAnnotation(value);
			return true;
		}
	}
	public Collection<OJAnnotationValue> getAnnotations() {
		return f_annotations.values();
	}
	public OJAnnotationValue putAnnotation(OJAnnotationValue value) {
		return f_annotations.put(value.getType(),value);
	}
	public OJAnnotationValue removeAnnotation(OJPathName type){
		return f_annotations.remove(type);
	}
	public String toJavaString() {
		StringBuilder sb = new StringBuilder();
		if (super.getComment() != null && super.getComment().length() > 0) {
			sb.append("/**\n");
			sb.append(super.getComment());
			sb.append("*/\n");
		}
		sb.append(JavaStringHelpers.indent(JavaUtil.collectionToJavaString(getAnnotations(), "\n"), 0));
		sb.append("\n");
		sb.append(getMyPackage().toJavaString());
		sb.append("\n");
		sb.append(JavaStringHelpers.indent(imports(), 0));
		return sb.toString();
	}
	private String imports() {
		StringBuilder sb = new StringBuilder();
		for (OJPathName path : getImports()) {
			if (getMyPackage().getPathName().equals(path.getHead())) {
				// already visible
			} else {
				sb.append("import " + path.toString() + ";\n");
			}
		}
		return sb.toString();
	}
	private Collection<OJPathName> getImports() {
		Collection<OJPathName> results = new HashSet<OJPathName>();
		for (OJAnnotationValue an : getAnnotations()) {
			results.addAll(an.getAllTypesUsed());
		}
		return results;
	}
	public OJAnnotatedPackageInfo getDeepCopy(OJAnnotatedPackageInfo owner) {
		OJAnnotatedPackageInfo copy = new OJAnnotatedPackageInfo(getName());
		copyDeepInfoInto(owner, copy);
		return copy;
	}
	protected void copyDeepInfoInto(OJAnnotatedPackageInfo owner, OJAnnotatedPackageInfo copy) {
		for (OJAnnotationValue annotation : this.getAnnotations()) {
			copy.addAnnotationIfNew(annotation.getDeepCopy());
		}
	}
	public OJAnnotationValue findAnnotation(OJPathName ojPathName) {
		return AnnotationHelper.getAnnotation(this, ojPathName);
	}
	public OJPackage getMyPackage(){
		return myPackage;
	}
	public void setMyPackage(OJPackage myPackage){
		this.myPackage = myPackage;
	}
	@Override
	public void renameAll(Set<OJPathName> match,String suffix){
		for(OJAnnotationValue a:getAnnotations()){
			a.renameAll(match, suffix);
		}
		
	}
}
