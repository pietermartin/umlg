package org.umlg.java.metamodel;

import java.util.ArrayList;
import java.util.Iterator;

import org.umlg.java.metamodel.generated.OJInterfaceGEN;
import org.umlg.java.metamodel.utilities.JavaStringHelpers;



public class OJInterface extends OJInterfaceGEN {
	/******************************************************
	 * The constructor for this classifier.
	*******************************************************/	
	public OJInterface() {
		super();
		setVisibility(OJVisibilityKind.PUBLIC);
	}

	public void calcImports() {
		super.calcImports(); // does operations
		// super interfaces
		Iterator it = getSuperInterfaces().iterator();
		while( it.hasNext()) {
			OJPathName intf = (OJPathName) it.next();
			this.addToImports(intf);
		}
	}
	
	public String toJavaString(){
		this.calcImports();
		StringBuilder classInfo = new StringBuilder();
		classInfo.append(getMyPackage().toJavaString());
		classInfo.append("\n");
		classInfo.append(imports());
		classInfo.append("\n");
		if (!getComment().equals("")){
			addJavaDocComment(classInfo);
		}
		if (this.isAbstract()) {
			classInfo.append("abstract ");
		}
		classInfo.append(visToJava(this) + " ");
		classInfo.append("interface " + getName());
		classInfo.append(superInterfaces());
		classInfo.append(" {\n");
		classInfo.append(JavaStringHelpers.indent(operations(),1));
		classInfo.append("\n");
		classInfo.append("}");
		return classInfo.toString();
	}
	
	private StringBuilder superInterfaces() {
		StringBuilder result = new StringBuilder();
		if (this.getSuperInterfaces().size() > 0) {
			Iterator it = this.getSuperInterfaces().iterator();
			boolean first = true;
			while (it.hasNext()) {
				OJPathName elem = (OJPathName) it.next();
				if (first) {
					result.append(" extends ");
					first = false;
				} else { 
					result.append(", ");
				}
				result.append(elem.getLast());
			}
		}
		return result;
	}
	
	public OJInterface getDeepCopy(OJPackage owner) {
		OJInterface copy = new OJInterface();
		copy.setMyPackage(owner);
		copyDeepInfoInto(copy);
		return copy;
	}
	protected void copyDeepInfoInto(OJInterface copy) {
		super.copyDeepInfoInto(copy);
		Iterator superInterfacesIt = new ArrayList<OJPathName>(getSuperInterfaces()).iterator();
		while ( superInterfacesIt.hasNext() ) {
			OJPathName elem = (OJPathName) superInterfacesIt.next();
			copy.addToSuperInterfaces(elem.getDeepCopy());
		}
	}

}