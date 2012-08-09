package org.opaeum.java.metamodel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.opaeum.java.metamodel.annotation.OJAnnotatedPackageInfo;
import org.opaeum.java.metamodel.generated.OJPackageGEN;



public class OJPackage extends OJPackageGEN {
	private List<OJAnnotatedPackageInfo> packageInfos = new ArrayList<OJAnnotatedPackageInfo>();
	/******************************************************
	 * The constructor for this classifier.
	*******************************************************/	
	public OJPackage() {
		super();
	}
	
	@Override
	public void finalize(){
		super.finalize();
	}
	public OJPackage(String name){
		this.setName(name);
	}
	public void addToPackageInfo(OJAnnotatedPackageInfo pk){
		packageInfos.add(pk);
		pk.setMyPackage(this);
	}
	public String toJavaString(){
		StringBuilder packInfo = new StringBuilder();
		if (!getPathName().toJavaString().equals("")) {
			packInfo.append("package " + getPathName().toJavaString() + ";");
		}
		return packInfo.toString();
	}
	public String toString() {
		return getPathName().toString();
	}
	
	public OJPathName getPathName() {
		OJPathName result = null;
		if (getParent() != null) {
			result = this.getParent().getPathName().append(this.getName());
		} else {
			result = new OJPathName(getName());
		}
		return result;
	}

	public OJClassifier findIntfOrCls(OJPathName path) {
		OJClassifier result = null;
		result = findInterface(path);
		if ( result == null ) {
			result = findClass(path);
		}
		return result;
	}

	/**
	 * @param name
	 * @return
	 */
	public OJInterface findInterface(OJPathName path) {
		if (path == null) return null;
		if( path.isSingleName() ){
			return findLocalInterface( path.getLast() );
		} else {
			OJPackage first = findLocalPackage( path.getFirst() );
			if( first != null) {
				return first.findInterface(path.getTail());
		    }
		}
		return null;
	}
	/**
	 * @param name
	 * @return
	 */
	public OJPackage findPackage(OJPathName path) {
		if (path == null) return null;
		if( path.isSingleName() ){
			return findLocalPackage( path.getLast() );
		} else {
			OJPackage first = findLocalPackage( path.getFirst() );
			if( first != null) {
				return first.findPackage(path.getTail());
		    }
		}
		return null;
	}
	/**
	 * @param string
	 * @return
	 */
	private OJInterface findLocalInterface(String string) {
		OJInterface result = null;
		Iterator it = getInterfaces().iterator();
		while (it.hasNext()){
			OJInterface elem = (OJInterface) it.next();
			if (elem.getName().equals(string)){
				result = elem;
			}
		}
		return result;
	}
	
	/**
	 * @param name
	 * @return
	 */
	public OJClass findClass(OJPathName path) {
		if (path == null) return null;
		if( path.isSingleName() ){
			return findLocalClass( path.getLast() );
		} else {
			OJPackage first = findLocalPackage( path.getFirst() );
			if( first != null) {
				return first.findClass(path.getTail());
		    }
		}
		return null;
	}
	/**
	 * @param string
	 * @return
	 */
	protected OJClass findLocalClass(String string) {
		OJClass result = null;
		Iterator it = getClasses().iterator();
		while (it.hasNext()){
			OJClass elem = (OJClass) it.next();
			if (elem.getName().equals(string)){
				result = elem;
			}
		}
		return result;
	}
	/**
	 * @param string
	 * @return
	 */
	protected OJPackage findLocalPackage(String string) {
		OJPackage result = null;
		Iterator it = getSubpackages().iterator();
		while (it.hasNext()){
			OJPackage elem = (OJPackage) it.next();
			if (elem.getName().equals(string)){
				result = elem;
			}
		}
		return result;
	}
	
	public OJPackage getDeepCopy(OJPackage owner) {
		OJPackage copy = new OJPackage();
		copyDeepInfoInto(owner, copy);
		return copy;		
	}

	protected void copyDeepInfoInto(OJPackage owner, OJPackage copy) {
		super.copyDeepInfoInto(copy);
		Iterator classesIt = new ArrayList<OJClass>(getClasses()).iterator();
		while ( classesIt.hasNext() ) {
			OJClass elem = (OJClass) classesIt.next();
			copy.addToClasses(elem.getDeepCopy(copy));
		}
		Iterator interfacesIt = new ArrayList<OJInterface>(getInterfaces()).iterator();
		while ( interfacesIt.hasNext() ) {
			OJInterface elem = (OJInterface) interfacesIt.next();
			copy.addToInterfaces(elem);
		}
		if ( owner != null ) {
			copy.setParent(owner);
		}
		Iterator subpackagesIt = new ArrayList<OJPackage>(getSubpackages()).iterator();
		while ( subpackagesIt.hasNext() ) {
			OJPackage elem = (OJPackage) subpackagesIt.next();
			copy.addToSubpackages(elem.getDeepCopy(owner));
		}		
	}
	@Override
	public void renameAll(Set<OJPathName> match,String suffix){
		for(OJClass ojClass:getClasses()){
			ojClass.renameAll(match, suffix);
		}
		for(OJAnnotatedPackageInfo pi:packageInfos){
			pi.renameAll(match, suffix);
		}
		Set<OJPackage> subpackages = getSubpackages();
		for(OJPackage p:subpackages){
			p.renameAll(match, suffix);
		}
	}
	public void release(){
		setParent(null);
		this.packageInfos.clear();
		for(OJClass ojClass:new ArrayList<OJClass>( getClasses())){
			ojClass.release();
		}
		for(OJPackage p:new ArrayList<OJPackage>(getSubpackages())){
			p.release();
		}
		
	}

}