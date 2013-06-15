package org.umlg.java.metamodel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.umlg.java.metamodel.generated.OJOperationGEN;
import org.umlg.java.metamodel.utilities.JavaStringHelpers;

@SuppressWarnings({
	"rawtypes"
})
public class OJOperation extends OJOperationGEN{
	/******************************************************
	 * The constructor for this classifier.
	 *******************************************************/
	public OJOperation(){
		super();
		this.setBody(new OJBlock());
		this.setVisibility(OJVisibilityKind.PUBLIC);
		this.setReturnType(new OJPathName("void"));
	}
	public String getSignature(){
		StringBuilder sb = new StringBuilder(getName());
		sb.append("(");
		for(OJParameter p:getParameters()){
			sb.append(p.getType().getLast());
			sb.append(",");
		}
		sb.setCharAt(sb.length() - 1, ')');
		return sb.toString();
	}
	/******************************************************
	 * The following operations are the implementations of the operations defined for this classifier.
	 *******************************************************/
	public void addParam(String name,OJPathName type){
		OJParameter param = new OJParameter();
		param.setName(name);
		param.setType(type);
		this.addToParameters(param);
	}
	public void addParam(String name,String type){
		OJPathName path = new OJPathName(type);
		this.addParam(name, path);
	}
	public void addToThrows(String type){
		OJPathName path = new OJPathName(type);
		this.addToThrows(path);
	}
	/******************************************************
	 * End of implemented operations.
	 *******************************************************/
	public String toJavaString(){
		StringBuilder result = new StringBuilder();
		if(!getComment().equals("")){
			addJavaDocComment(result);
		}
		if(this.getNeedsSuppress()){
			result.append("@SuppressWarnings(\"unchecked\")\n");
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
		if(!getThrows().isEmpty())
			result.append(" throws " + exceptionsToJava(this));
		// body
		if(getOwner() instanceof OJInterface || this.isAbstract()){
			result.append(";\n");
		}else{
			result.append(" {\n");
			StringBuilder bodyStr = new StringBuilder();
			String actualBody = getBody().toJavaString();
			// if (actualBody.trim().length() == 0) {
			// actualBody = getReturnType().getLast() + " result;\n"
			// + "return result;";
			// }
			bodyStr.append(actualBody);
			result.append(JavaStringHelpers.indent(bodyStr, 1));
			if(result.charAt(result.length() - 1) == '\n'){
				result.deleteCharAt(result.length() - 1);
			}
			// closing bracket
			result.append("\n}\n");
		}
		return result.toString();
	}
	/**
	 * @param result
	 */
	protected void addJavaDocComment(StringBuilder result){
		String comment = getComment();
		comment = JavaStringHelpers.replaceAllSubstrings(comment, "\n", "\n * ");
		result.append("/**\n * " + comment);
		boolean first = true;
		Iterator it = getParameters().iterator();
		while(it.hasNext()){
			OJParameter par = (OJParameter) it.next();
			String paramStr = "@param " + par.getName() + " " + par.getComment();
			if(first){
				result.append("\n * ");
				first = false;
			}
			result.append("\n * " + paramStr);
		}
		result.append("\n */\n");
	}
	protected StringBuilder paramsToJava(OJOperation op){
		StringBuilder result = new StringBuilder();
		Iterator it = op.getParameters().iterator();
		boolean first = true;
		while(it.hasNext()){
			OJParameter elem = (OJParameter) it.next();
			if(first){
				first = false;
			}else{
				result.append(", ");
			}
			result.append(elem.toJavaString());
		}
		return result;
	}
	protected StringBuilder exceptionsToJava(OJOperation op){
		StringBuilder result = new StringBuilder();
		Iterator it = op.getThrows().iterator();
		boolean first = true;
		while(it.hasNext()){
			OJPathName elem = (OJPathName) it.next();
			if(first){
				first = false;
			}else{
				result.append(", ");
			}
			result.append(elem.getLast());
		}
		return result;
	}
	public static StringBuilder paramsToActuals(OJOperation op){
		StringBuilder result = new StringBuilder();
		Iterator it = op.getParameters().iterator();
		boolean first = true;
		while(it.hasNext()){
			OJParameter elem = (OJParameter) it.next();
			if(first){
				first = false;
			}else{
				result.append(", ");
			}
			result.append(elem.getName());
		}
		return result;
	}
	// TODO find out whether we can use the generated copy method
	public OJOperation getCopy(){
		OJOperation result = new OJOperation();
		copyValues(result);
		return result;
	}
	public OJOperation getDeepCopy(){
		OJOperation result = new OJOperation();
		copyValuesDeep(result);
		return result;
	}
	protected void copyValuesDeep(OJOperation result){
		result.setComment(getComment());
		result.setBody(this.getBody().getDeepCopy());
		result.setReturnType(this.getReturnType().getDeepCopy());
		result.setFinal(this.isFinal());
		result.setStatic(this.isStatic());
		result.setVolatile(this.isVolatile());
		result.setName(this.getName());
		result.setAbstract(isAbstract());
		for(OJParameter ojParameter:this.getParameters()){
			result.addToParameters(ojParameter.getDeepCopy());
		}
		result.setVisibility(this.getVisibility());
	}
	protected void copyValues(OJOperation result){
		result.setComment(getComment());
		result.setBody(this.getBody().getCopy());
		result.setReturnType(this.getReturnType());
		result.setFinal(this.isFinal());
		result.setStatic(this.isStatic());
		result.setVolatile(this.isVolatile());
		result.setName(this.getName());
		List<OJParameter> params = new ArrayList<OJParameter>(this.getParameters());
		result.setParameters(params);
		result.setVisibility(this.getVisibility());
	}
	public boolean isEqual(String name,List /* (OJPathName) */types){
		boolean result = false;
		if(this.getName().equals(name)){
			List<OJParameter> myPars = this.getParameters();
			if((myPars.size() == 0) && (types.size() == 0)){
				result = true;
			}else{
				if(myPars.size() == types.size()){
					Iterator parsIt = myPars.iterator();
					Iterator typesIt = types.iterator();
					boolean loopResult = true;
					while(parsIt.hasNext()){
						OJParameter par = (OJParameter) parsIt.next();
						OJPathName type = (OJPathName) typesIt.next();
						if(!(par.getType().equals(type))){
							loopResult = false;
						}
					}
					result = loopResult;
				}
			}
		}
		return result;
	}
	public List<OJPathName> getParamTypes(){
		List<OJPathName> result = new ArrayList<OJPathName>();
		Iterator it = this.getParameters().iterator();
		while(it.hasNext()){
			OJParameter par = (OJParameter) it.next();
			result.add(par.getType());
		}
		return result;
	}
	public void renameAll(Set<OJPathName> renamePathNames,String newName){
		getReturnType().renameAll(renamePathNames, newName);
		for(OJParameter parameter:getParameters()){
			parameter.renameAll(renamePathNames, newName);
		}
		getBody().renameAll(renamePathNames, newName);
	}
	@Override
	public boolean equals(Object other){
		if(other instanceof OJOperation){
			OJOperation o = (OJOperation) other;
			List<OJPathName> oParamTypes = o.getParamTypes();
			if(o.getOwner().equals(getOwner()) && o.getName().equals(getName())){
				return paramsEquals(oParamTypes);
			}
		}
		return false;
	}
	public boolean paramsEquals(List<OJPathName> oParamTypes){
		List<OJPathName> paramTypes = getParamTypes();
		boolean b = true;
		if(oParamTypes.size() == paramTypes.size()){
			for(int i = 0;i < paramTypes.size();i++){
				if(!paramTypes.get(i).equals(oParamTypes.get(i))){
					b = false;
					break;
				}
			}
		}else{
			b = false;
		}
		return b;
	}
}