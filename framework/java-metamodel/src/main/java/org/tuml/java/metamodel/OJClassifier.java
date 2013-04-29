package org.tuml.java.metamodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.tuml.java.metamodel.annotation.OJAnnotatedOperation;
import org.tuml.java.metamodel.generated.OJClassifierGEN;
import org.tuml.java.metamodel.utilities.JavaStringHelpers;
import org.tuml.java.metamodel.utilities.JavaUtil;
import org.tuml.java.metamodel.utilities.OJOperationComparator;
import org.tuml.java.metamodel.utilities.OJPathNameComparator;

public class OJClassifier extends OJClassifierGEN {
	boolean isInnerClass = false;
	protected OJPackage f_myPackage;
	protected String suffix;

	/******************************************************
	 * The constructor for this classifier.
	 *******************************************************/
	public OJClassifier() {
		super();
	}

	public void calcImports() {
		// operations
		for (OJOperation oper : getOperations()) {
			addAll(oper.getParamTypes());
			this.addToImports(oper.getReturnType());
			addImportsRecursively(oper.getBody());
		}
	}

	public OJOperation getUniqueOperation(String name) {
		Set<OJOperation> set = super.f_operations.get(name);
		if (set != null && set.size() == 1) {
			return set.iterator().next();
		} else {
			return null;
		}
	}

	protected void addImportsRecursively(OJBlock body) {
		if (body != null) {
			for (OJField ojField : body.getLocals()) {
				this.addToImports(ojField.getType());
			}
			for (OJStatement s : body.getStatements()) {
				if (s instanceof OJIfStatement) {
					addImportsRecursively(((OJIfStatement) s).getThenPart());
					addImportsRecursively(((OJIfStatement) s).getElsePart());
				} else if (s instanceof OJBlock) {
					addImportsRecursively(((OJBlock) s));
				} else if (s instanceof OJTryStatement) {
					addImportsRecursively(((OJTryStatement) s).getTryPart());
					addImportsRecursively(((OJTryStatement) s).getCatchPart());
				} else if (s instanceof OJWhileStatement) {
					addImportsRecursively(((OJWhileStatement) s).getBody());
				} else if (s instanceof OJForStatement) {
					addImportsRecursively(((OJForStatement) s).getBody());
					addToImports(((OJForStatement) s).getElemType());
				} else if (s instanceof OJSwitchStatement) {
					for (OJSwitchCase ojSwitchCase : ((OJSwitchStatement) s).getCases()) {
						addImportsRecursively(ojSwitchCase.getBody());
					}
				}
			}
		}
	}

	private void addAll(List<OJPathName> types) {
		for (OJPathName type : types) {
			if (type != null) {
				this.addToImports(type);
				if (!type.getElementTypes().isEmpty()) {
					addAll(type.getElementTypes());
				}
			}
		}
	}

	public void addToImports(OJPathName path){
		if(path == null)
			return;
		if(path.isSingleName()){
			// do nothing, imported element is in same package
		}else if(path.getLast().equals("void")){
			// do nothing, no need to import "void"
		}else if(path.getHead().equals(new OJPathName("java.lang"))){
			// do nothing, no need to import "java.lang.*"
		}else if(path.getLast().equals("int")){
			// do nothing, no need to import "int"
		}else if(path.getLast().equals("Integer")){
			// do nothing, no need to import "Integer"
		}else if(path.getLast().equals("String")){
			// do nothing, no need to import "String"
		}else if(path.getLast().equals("Boolean")){
			// do nothing, no need to import "String"
		}else if(path.getLast().equals("boolean")){
			// do nothing, no need to import "boolean"
		}else if(path.getLast().equals("float")){
			// do nothing, no need to import "float"
		}else if(path.getLast().equals("Object")){
			// do nothing, no need to import "Object"
		}else if(path.getLast().charAt(path.getLast().length() - 1) == '>'){
			//some generic type, remove generic part
			String lastEntry = path.getLast();
			lastEntry = lastEntry.substring(0, lastEntry.indexOf("<"));
			OJPathName path2 = path.getCopy().getHead();
			path2.addToNames(lastEntry);
			this.addToImports(path2);
			for (OJPathName generic : path.getGenerics()) {				
				addToImports(generic);
			}
		}else if(path.getLast().charAt(path.getLast().length() - 1) == ']'){
			// some array type, remove '[]'
			String lastEntry = path.getLast();
			lastEntry = lastEntry.substring(0, lastEntry.length() - 2);
			OJPathName path2 = path.getCopy().getHead();
			path2.addToNames(lastEntry);
			this.addToImports(path2);
		}else{
			// check whether path is already present is performed by super
			super.addToImports(path);
			if(!path.getElementTypes().isEmpty()){
				addAll(path.getElementTypes());
			}
			for (OJPathName generic : path.getGenerics()) {				
				addToImports(generic);
			}
		}
		
	}

	public void addToImports(String pathName) {
		if (pathName == null)
			return;
		OJPathName path = new OJPathName(pathName);
		addToImports(path);
	}

	public int getUniqueNumber() {
		int i = super.getUniqueNumber() + 1;
		super.setUniqueNumber(i);
		return i;
	}

	/**
	 * 
	 */
	public OJOperation findToString() {
		OJOperation result = null;
		Iterator it = getOperations().iterator();
		while (it.hasNext()) {
			OJOperation oper = (OJOperation) it.next();
			if (oper.getName().equals("toString"))
				result = oper;
		}
		return result;
	}

	/**
	 * 
	 */
	public OJOperation findIdentOper() {
		OJOperation result = null;
		Iterator it = getOperations().iterator();
		while (it.hasNext()) {
			OJOperation oper = (OJOperation) it.next();
			if (oper.getName().equals("getIdentifyingString"))
				result = oper;
		}
		return result;
	}

	/******************************************************
	 * End of getters and setters.
	 *******************************************************/
	/**
	 * @param result
	 */
	protected void addJavaDocComment(StringBuilder result) {
		String comment = JavaStringHelpers.firstCharToUpper(getComment());
		result.append("/** " + comment);
		result.append("\n */\n");
	}

	/**
	 * @return
	 */
	protected StringBuilder operations() {
		// sort the operations on visibilityKind, then name
		List<OJOperation> temp = new ArrayList<OJOperation>(this.getOperations());
		Collections.sort(temp, new OJOperationComparator());
		//
		StringBuilder result = new StringBuilder();
		result.append(JavaUtil.collectionToJavaString(temp, "\n"));
		// if ( result.length() > 0) result.append("\n");
		return result;
	}

	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected StringBuilder imports() {
		// sort the imports by alphabeth
		Set myImports = new TreeSet(new OJPathNameComparator());
		myImports.addAll(this.getImports());
		//
		StringBuilder result = new StringBuilder();
		Iterator it = myImports.iterator();
		String prevPackageName = "";
		while (it.hasNext()) {
			OJPathName path = (OJPathName) it.next();
			if (!this.isInnerClass && this.getMyPackage().getPathName().equals(path.getHead())) {
				// do nothing, imported element is in same package
			} else {
				if (!path.getFirst().equals(prevPackageName)) {
					result.append("\n");
				}
				result.append("import " + path.toString() + ";\n");
				prevPackageName = path.getFirst();
			}
		}
		return result;
	}

	public OJAnnotatedOperation findOperation(String name) {
		return (OJAnnotatedOperation) findOperation(name, Collections.<OJPathName>emptyList());
	}

	public OJOperation findOperation(String name, List<OJPathName> types) {
		OJOperation result = null;
		Iterator it = getOperations().iterator();
		while (it.hasNext()) {
			OJOperation elem = (OJOperation) it.next();
			if (elem.isEqual(name, types))
				return elem;
		}
		return result;
	}

	public void copyDeepInfoInto(OJClassifier copy) {
		super.copyDeepInfoInto(copy);
		copy.setUniqueNumber(getUniqueNumber());
		copy.setDerived(isDerived());
		copy.setAbstract(isAbstract());
		Iterator operationsIt = new ArrayList<OJOperation>(getOperations()).iterator();
		while (operationsIt.hasNext()) {
			OJOperation elem = (OJOperation) operationsIt.next();
			copy.addToOperations(elem.getDeepCopy());
		}
		Iterator importsIt = new ArrayList<OJPathName>(getImports()).iterator();
		while (importsIt.hasNext()) {
			OJPathName elem = (OJPathName) importsIt.next();
			copy.addToImports(elem.getCopy());
		}
	}

	@Override
	public String getName() {
		if (suffix != null) {
			return super.getName() + suffix;
		} else {
			return super.getName();
		}
	}

	@Override
	public void renameAll(Set<OJPathName> renamePathNames, String suffix) {
		if (renamePathNames.contains(getPathName())) {
			this.suffix = suffix;
		}
		Set<OJPathName> newImports = new HashSet<OJPathName>();
		Collection<OJPathName> imports = getImports();
		for (OJPathName ojPathName : imports) {
			OJPathName newImport = ojPathName.getDeepCopy();
			newImport.renameAll(renamePathNames, suffix);
			newImports.add(newImport);
		}
		setImports(newImports);
		Collection<OJOperation> operations = getOperations();
		for (OJOperation ojOperation : operations) {
			ojOperation.renameAll(renamePathNames, suffix);
		}
	}
	
	public boolean isInnerClass() {
		return isInnerClass;
	}

	public void setInnerClass(boolean isInnerClass) {
		this.isInnerClass = isInnerClass;
	}
	
}