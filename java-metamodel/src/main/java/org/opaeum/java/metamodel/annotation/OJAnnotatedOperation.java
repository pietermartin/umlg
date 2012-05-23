package org.opaeum.java.metamodel.annotation;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.opaeum.java.metamodel.OJInterface;
import org.opaeum.java.metamodel.OJOperation;
import org.opaeum.java.metamodel.OJParameter;
import org.opaeum.java.metamodel.OJPathName;
import org.opaeum.java.metamodel.utilities.JavaStringHelpers;
import org.opaeum.java.metamodel.utilities.JavaUtil;

public class OJAnnotatedOperation extends OJOperation implements OJAnnotatedElement{
	public static final String RESULT = "result";
	Map<OJPathName, OJAnnotationValue> f_annotations = new TreeMap<OJPathName, OJAnnotationValue>();
	private OJAnnotatedField resultVariable;
	public OJAnnotatedOperation(String string,OJPathName ojPathName){
		this(string);
		setReturnType(ojPathName);
	}
	public OJAnnotatedOperation(String string){
		super();
		setName(string);
	}
	@Override
	public OJAnnotatedOperation getCopy(){
		OJAnnotatedOperation oper = new OJAnnotatedOperation(getName());
		super.copyValues(oper);
		oper.removeAllFromParameters();
		for(OJParameter f:getParameters()){
			oper.addToParameters((OJParameter) f.getCopy());
		}
		for(OJAnnotationValue v:getAnnotations()){
			oper.addAnnotationIfNew(v.getCopy());
		}
		// TODO copy exception
		return oper;
	}
	public boolean addAnnotationIfNew(OJAnnotationValue value){
		if(f_annotations.containsKey(value.getType())){
			return false;
		}else{
			putAnnotation(value);
			return true;
		}
	}
	public OJAnnotationValue removeAnnotation(OJPathName type){
		return f_annotations.remove(type);
	}
	public Collection<OJAnnotationValue> getAnnotations(){
		return f_annotations.values();
	}
	public OJAnnotationValue putAnnotation(OJAnnotationValue value){
		return f_annotations.put(value.getType(), value);
	}
	@Override
	public String toJavaString(){
		StringBuilder result = new StringBuilder();
		if(!getComment().equals("")){
			addJavaDocComment(result);
		}
		if(this.getNeedsSuppress()){
			result.append("@SuppressWarnings(\"unchecked\")\n");
		}
		if(getAnnotations().size() > 0){
			result.append(JavaStringHelpers.indent(JavaUtil.collectionToJavaString(getAnnotations(), "\n"), 0));
			result.append("\n");
		}
		// signature
		if(this.isAbstract()){
			result.append("abstract ");
		}
		result.append(visToJava(this) + " ");
		if(this.getGenericTypeParam() != null){
			result.append("<" + this.getGenericTypeParam().getLast() + "> ");
		}
		result.append(getReturnType().getCollectionTypeName());
		result.append(" " + getName());
		// params
		result.append("(" + paramsToJava(this) + ")");
		if(!getThrows().isEmpty()){
			result.append(" throws " + exceptionsToJava(this));
		}
		if(getOwner() instanceof OJAnnotatedInterface || getOwner() instanceof OJInterface || this.isAbstract()){
			result.append(";\n");
		}else{
			result.append(" {\n");
			StringBuilder bodyStr = new StringBuilder();
			if(resultVariable != null){
				if(resultVariable.getType() == null){
					resultVariable.setType(getReturnType());
				}
				bodyStr.append(resultVariable.toJavaString());
				bodyStr.append("\n");
			}
			bodyStr.append(getBody().toJavaString());
			if(resultVariable != null){
				bodyStr.append("\nreturn result;");
			}
			result.append(JavaStringHelpers.indent(bodyStr, 1));
			if(result.charAt(result.length() - 1) == '\n'){
				result.deleteCharAt(result.length() - 1);
			}
			// closing bracket
			result.append("\n}\n");
		}
		return result.toString();
	}
	public void renameAll(Set<OJPathName> renamePathNames,String suffix){
		super.renameAll(renamePathNames, suffix);
		for(OJAnnotationValue annotation:getAnnotations()){
			annotation.renameAll(renamePathNames, suffix);
		}
		if(resultVariable!=null){
			resultVariable.renameAll(renamePathNames, suffix);
		}
	}
	public OJOperation getDeepCopy(){
		OJAnnotatedOperation result = new OJAnnotatedOperation(getName());
		copyValuesDeep(result);
		return result;
	}
	public OJAnnotationValue findAnnotation(OJPathName ojPathName){
		return AnnotationHelper.getAnnotation(this, ojPathName);
	}
	public void initializeResultVariable(String initialValue){
		if(initialValue == null){
			resultVariable = null;
		}else{
			resultVariable = new OJAnnotatedField("result", getReturnType());
			resultVariable.setInitExp(initialValue);
		}
	}
	public OJAnnotatedField getResultVariable(){
		return this.resultVariable;
	}
	public OJParameter findParameter(String fieldname){
		for(OJParameter p:getParameters()){
			if(p.getName().equals(fieldname)){
				return p;
			}
		}
		return null;
	}
}
