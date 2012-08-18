package org.opaeum.java.metamodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.opaeum.java.metamodel.generated.OJBlockGEN;
import org.opaeum.java.metamodel.utilities.JavaUtil;

public class OJBlock extends OJBlockGEN{
	/******************************************************
	 * The constructor for this classifier.
	 *******************************************************/
	public OJBlock(){
		super();
	}
	/**
	 * Constructor for OJBlock
	 * 
	 * @param name
	 * @param comment
	 */
	public OJBlock(String name,String comment){
		// super(name, comment);
	}
	public void addToStatements(String name, String str){
		if(str.length() == 0)
			return;
		OJSimpleStatement stat = new OJSimpleStatement();
		stat.setName(name);
		stat.setExpression(str);
		this.addToStatements(stat);
	}

	public void addToStatements(String str){
		if(str.length() == 0)
			return;
		OJSimpleStatement stat = new OJSimpleStatement();
		stat.setExpression(str);
		this.addToStatements(stat);
	}
	public void addToStatements(int index,OJStatement statement){
		getStatements().add(index, statement);
	}
	public void addToStatements(int indexOfRemoveVertex,String expression){
		if(expression.length() == 0)
			return;
		OJSimpleStatement stat = new OJSimpleStatement();
		stat.setExpression(expression);
		this.addToStatements(indexOfRemoveVertex, stat);
	}
	public String toJavaString(){
		String result = "";
		if(getComment().length() != 0)
			result = "/* " + getComment() + "*/\n";
		if(!getLocals().isEmpty()){
			result = result + JavaUtil.collectionToJavaString(getLocals(), "\n") + "\n";
		}
		result = result + JavaUtil.collectionToJavaString(getStatements(), "\n");
		return result;
	}
	/**
	 * @return
	 */
	public OJBlock getCopy(){
		OJBlock newBody = new OJBlock();
		List<OJStatement> stats = new ArrayList<OJStatement>(this.getStatements());
		newBody.setStatements(stats);
		return newBody;
	}
	public OJBlock getDeepCopy(){
		OJBlock copy = new OJBlock();
		copyDeepInfoInto(copy);
		return copy;
	}
	public void copyDeepInfoInto(OJBlock copy){
		for(OJStatement statement:getStatements()){
			copy.addToStatements(statement.getDeepCopy());
		}
		for(OJField ojField:getLocals()){
			copy.addToLocals(ojField.getDeepCopy());
		}
	}
	public void renameAll(Set<OJPathName> renamePathNames,String newName){

		for(OJStatement statement:getStatements()){
			statement.renameAll(renamePathNames, newName);
		}
		for(OJField ojField:getLocals()){
			ojField.renameAll(renamePathNames, newName);
		}
	}
	public OJStatement findStatement(String name){
		for(OJStatement statement:getStatements()){
			if(statement.getName() != null && statement.getName().equals(name)){
				return statement;
			}
		}
		return null;
	}
	public OJStatement findStatementRecursive(String name){
		for(OJStatement statement:getStatements()){
			if(statement.getName() != null && statement.getName().equals(name)){
				return statement;
			}
			if(statement instanceof OJIfStatement){
				OJIfStatement ifs = (OJIfStatement) statement;
				OJStatement s = ifs.getThenPart().findStatementRecursive(name);
				if(s == null && ifs.getElsePart()!=null){
					s = ifs.getElsePart().findStatementRecursive(name);
				}
				if(s != null){
					return s;
				}
			}else if(statement instanceof OJForStatement){
				OJForStatement ifs = (OJForStatement) statement;
				OJStatement s = ifs.getBody().findStatementRecursive(name);
				if(s != null){
					return s;
				}
			}else{
				// TODO
			}
		}
		return null;
	}
	public OJField findLocal(String name){
		for(OJField field:getLocals()){
			if(field.getName().equals(name)){
				return field;
			}
		}
		return null;
	}
}