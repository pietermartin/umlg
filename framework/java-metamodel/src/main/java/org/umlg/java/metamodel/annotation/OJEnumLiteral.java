package org.umlg.java.metamodel.annotation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.umlg.java.metamodel.OJElement;
import org.umlg.java.metamodel.OJField;
import org.umlg.java.metamodel.OJPathName;

/**
 * An enumeration literal is more than just a name. Java allows enumerations to have fields. The standard idiom for initialisations of such fields
 * is to do it from the constructor. Every enumeration literal should therefor have enough information to invoke a constructor with
 * the corret initialisation for its attribute values. 
 * @author ampie
 *
 */
public class OJEnumLiteral extends OJElement  implements OJAnnotatedElement{
	List<OJField> attributeValues= new ArrayList<OJField>();
	Map<OJPathName, OJAnnotationValue> f_annotations = new TreeMap<OJPathName, OJAnnotationValue>();

	public OJEnumLiteral(String name) {
		super();
		super.setName(name);
	}

	public OJEnumLiteral() {
		super();
	}


	@Override
	public String toJavaString() {
		if(this.attributeValues.isEmpty()){
			return getName();
		}else{
			StringBuilder sb = new StringBuilder(getName());
			sb.append('(');
			Iterator<OJField> iter = this.attributeValues.iterator();
			while (iter.hasNext()) {
				OJField a = iter.next();
				sb.append("/* " + a.getName() + " */ ");
				sb.append(a.getInitExp());
				if(iter.hasNext()){
					sb.append(',');
				}
			}
			sb.append(')');
			return sb.toString();
		}
	}
	public void addToAttributeValues(OJField field){
		this.attributeValues.add(field);
	}
	public OJField findAttributeValue(String fieldName){
		for(OJField f:this.attributeValues){
			if(f.getName().equals(fieldName)){
				return f;
			}
		}
		OJField value=new OJField();
		value.setName(fieldName);
		this.attributeValues.add(value);
		return value;
	}

	public OJAnnotationValue findAnnotation(OJPathName path) {
		return AnnotationHelper.getAnnotation(this, path);
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

	public OJAnnotationValue removeAnnotation(OJPathName type) {
		return f_annotations.remove(type);
	}

	@Override
	public void renameAll(Set<OJPathName> match,String suffix){
		for(OJAnnotationValue a:getAnnotations()){
			a.renameAll(match, suffix);
		}
		for(OJField ojField:attributeValues){
			ojField.setInitExp(replaceAll(ojField.getInitExp(), match, suffix));
		}
		
	}

}
