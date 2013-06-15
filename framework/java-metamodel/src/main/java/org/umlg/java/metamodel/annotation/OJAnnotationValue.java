package org.umlg.java.metamodel.annotation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.umlg.java.metamodel.OJPathName;

/**
 * An instantiation of an annotation, i.e. an annotation assignment or an annotation appplication. Has a type and could have values for its
 * atributes. If no attributes were assigned but values were added directly to the annotation value, the following syntax will be used:
 * '@AnAnnotation({"value1","value2"}). If attribute values were added, the conventional syntax will be used:
 * '@AnAnnotation(attribute1={"value1","value2"}, attribute2="value")
 * 
 * @author ampie
 * 
 */
public class OJAnnotationValue extends OJMetaValue{
	private OJPathName type;
	private SortedMap<String,OJAnnotationAttributeValue> attributes = new TreeMap<String,OJAnnotationAttributeValue>();
	public OJAnnotationValue(){
		super();
	}
	public OJAnnotationValue(OJPathName type){
		this.type = type;
	}
	public OJAnnotationValue(OJPathName type,Boolean value){
		super(value);
		super.setName(type.getLast());
		this.type = type;
	}

	public OJAnnotationValue(OJPathName type,Number value){
		super(value);
		super.setName(type.getLast());
		this.type = type;
	}
	public OJAnnotationValue(OJPathName type,OJAnnotationValue value){
		super(value);
		super.setName(type.getLast());
		this.type = type;
	}
	public OJAnnotationValue(OJPathName type,OJPathName value){
		super(value);
		super.setName(type.getLast());
		this.type = type;
	}
	public OJAnnotationValue(OJPathName type,String value){
		super(value);
		super.setName(type.getLast());
		this.type = type;
	}
	public OJAnnotationValue(OJPathName type,OJEnumValue value){
		super(value);
		super.setName(type.getLast());
		this.type = type;
	}
	public OJAnnotationAttributeValue putAttribute(String name,Boolean booleanValue){
		return putAttribute(new OJAnnotationAttributeValue(name, booleanValue));
	}
	public OJAnnotationAttributeValue putAttribute(String name,Number numberValue){
		return putAttribute(new OJAnnotationAttributeValue(name, numberValue));
	}
	public OJAnnotationAttributeValue putAttribute(String name,OJAnnotationValue annotationValue){
		return putAttribute(new OJAnnotationAttributeValue(name, annotationValue));
	}
	public OJAnnotationAttributeValue putAttribute(String name,OJPathName classValue){
		return putAttribute(new OJAnnotationAttributeValue(name, classValue));
	}
	public OJAnnotationAttributeValue putAttribute(String name,OJEnumValue ojEnumValue){
		return putAttribute(new OJAnnotationAttributeValue(name, ojEnumValue));
	}
	public OJAnnotationAttributeValue putAttribute(String name,String stringValue){
		return putAttribute(new OJAnnotationAttributeValue(name, stringValue));
	}
	public OJAnnotationAttributeValue putEnumAttribute(String name,OJEnumValue enumValue){
		return putAttribute(new OJAnnotationAttributeValue(name, enumValue));
	}
	public Set<OJPathName> getAllTypesUsed(){
		Set<OJPathName> set = new HashSet<OJPathName>();
		addTypesUsed(set);
		return set;
	}
	@Override
	public void addTypesUsed(Set<OJPathName> s){
		if(isImportType()){
			s.add(this.type);
		}
		super.addTypesUsed(s);
		for(OJAnnotationAttributeValue o:this.attributes.values()){
			o.addTypesUsed(s);
		}
	}
	@Override
	public String toJavaString(){
		String referencedName = isImportType() ? this.type.getLast() :this.type.toJavaString();
		if(this.attributes.isEmpty() && super.values.size() > 0){
			return "@" + referencedName + "(" + super.toJavaValueExpression() + ")";
		}else if(this.attributes.size() > 0){
			StringBuilder sb = new StringBuilder();
			sb.append("@");
			sb.append(referencedName);
			sb.append("(");
			Iterator<OJAnnotationAttributeValue> a = this.attributes.values().iterator();
			while(a.hasNext()){
				OJMetaValue attr = a.next();
				sb.append(attr.toJavaString());
				if(a.hasNext()){
					sb.append(",");
				}
			}
			sb.append(")");
			return sb.toString();
		}else{
			return "@" + referencedName;
		}
	}
	public OJAnnotationAttributeValue putAttribute(OJAnnotationAttributeValue annotationAttribute){
		return this.attributes.put(annotationAttribute.getName(), annotationAttribute);
	}
	public OJAnnotationAttributeValue removeAttribute(OJAnnotationAttributeValue annotationAttribute){
		return this.attributes.remove(annotationAttribute);
	}
	public OJPathName getType(){
		return type;
	}
	public boolean hasAttribute(String string){
		OJAnnotationAttributeValue result = findAttribute(string);
		return result != null;
	}
	public OJAnnotationAttributeValue findAttribute(String string){
		return attributes.get(string);
	}
	public void setType(OJPathName pathName){
		this.type = pathName;
	}
	public OJAnnotationValue getCopy(){
		OJAnnotationValue result = new OJAnnotationValue();
		copyInfoInto(result);
		return result;
	}
	protected void copyInfoInto(OJAnnotationValue result){
		super.copyInfoInto(result);
		result.type = type.getCopy();
		result.attributes = new TreeMap<String,OJAnnotationAttributeValue>();
		for(OJAnnotationAttributeValue v:this.attributes.values()){
			result.attributes.put(v.getName(), v.getCopy());
		}
	}
	public OJAnnotationValue getDeepCopy(){
		OJAnnotationValue result = new OJAnnotationValue();
		copyDeepInfoInto(result);
		return result;
	}
	protected void copyDeepInfoInto(OJAnnotationValue result){
		super.copyDeepInfoInto(result);
		result.type = type.getDeepCopy();
		result.attributes = new TreeMap<String,OJAnnotationAttributeValue>();
		for(OJAnnotationAttributeValue v:this.attributes.values()){
			result.attributes.put(v.getName(), v.getDeepCopy());
		}
	}
	public void renameAll(Set<OJPathName> renamePathNames,String newName){
		super.renameAll(renamePathNames, newName);
		for(OJAnnotationAttributeValue attr:this.attributes.values()){
			attr.renameAll(renamePathNames, newName);
		}
	}
	public void removeAttribute(String string){
		attributes.remove(string);
	}
}
