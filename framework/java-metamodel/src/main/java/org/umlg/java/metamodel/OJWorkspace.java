package org.umlg.java.metamodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.umlg.java.metamodel.utilities.InvariantError;

public class OJWorkspace extends OJElement{
	private Set<OJPackage> f_rootPackages = new HashSet<OJPackage>();
	public OJWorkspace(){
		super();
	}
	public void release(){
		for(OJPackage p:this.getRootPackages()){
			p.release();
		}
	}
	public OJClass findClass(OJPathName path){
		OJPackage pkg = this.findLocalPackage(path.getFirst());
		if(pkg == null){
			return null;
		}else{
			return pkg.findClass(path.getTail());
		}
	}
	public OJPackage findPackage(OJPathName path){
		if(path.isSingleName()){
			return this.findLocalPackage(path.getLast());
		}else{
			OJPackage lp = this.findLocalPackage(path.getFirst());
			if(lp != null){
				return lp.findPackage(path.getTail());
			}else{
				return null;
			}
		}
	}
	protected OJPackage findLocalPackage(String name){
		return any2(name);
	}
	public void setRootPackages(Set<OJPackage> elements){
		if(this.f_rootPackages != elements){
			this.f_rootPackages = elements;
		}
	}
	public synchronized void addToRootPackages(OJPackage element){
		if(element == null){
			return;
		}
		if(this.f_rootPackages.contains(element)){
			this.f_rootPackages.remove(element);
		}
		this.f_rootPackages.add(element);
	}
	public void removeFromRootPackages(OJPackage element){
		if(element == null){
			return;
		}
		this.f_rootPackages.remove(element);
	}
	public Set<OJPackage> getRootPackages(){
		if(f_rootPackages != null){
			return Collections.unmodifiableSet(f_rootPackages);
		}else{
			return null;
		}
	}
	public void z_internalAddToRootPackages(OJPackage element){
		for(OJPackage pkg:f_rootPackages){
			if(pkg.getName().equals(element.getName()))
				throw new RuntimeException();
		}
		this.f_rootPackages.add(element);
	}
	public void z_internalRemoveFromRootPackages(OJPackage element){
		this.f_rootPackages.remove(element);
	}
	public void addToRootPackages(Collection<OJPackage> newElems){
		Iterator it = newElems.iterator();
		while((it.hasNext())){
			Object item = it.next();
			if(item instanceof OJPackage){
				this.addToRootPackages((OJPackage) item);
			}
		}
	}
	public void removeFromRootPackages(Collection<OJPackage> oldElems){
		Iterator it = oldElems.iterator();
		while((it.hasNext())){
			Object item = it.next();
			if(item instanceof OJPackage){
				this.removeFromRootPackages((OJPackage) item);
			}
		}
	}
	public void removeAllFromRootPackages(){
		Iterator it = new HashSet<OJPackage>(getRootPackages()).iterator();
		while((it.hasNext())){
			Object item = it.next();
			if(item instanceof OJPackage){
				this.removeFromRootPackages((OJPackage) item);
			}
		}
	}
	private OJPackage any2(String name){
		OJPackage result = null;
		Iterator it = this.getRootPackages().iterator();
		while(it.hasNext()){
			OJPackage c = (OJPackage) it.next();
			if(c.getName().equals(name)){
				return c;
			}
		}
		return result;
	}
	public List<InvariantError> checkAllInvariants(){
		List<InvariantError> result = new ArrayList<InvariantError>();
		return result;
	}
	public String toString(){
		String result = "";
		result = super.toString();
		return result;
	}
	public String getIdString(){
		String result = "";
		result = super.getIdString();
		return result;
	}
	public OJElement getCopy(){
		OJWorkspace result = new OJWorkspace();
		this.copyInfoInto(result);
		return result;
	}
	public void copyInfoInto(OJWorkspace copy){
		super.copyInfoInto(copy);
		Iterator<OJPackage> rootPackagesIt = new ArrayList<OJPackage>(getRootPackages()).iterator();
		while(rootPackagesIt.hasNext()){
			OJPackage elem = (OJPackage) rootPackagesIt.next();
			copy.addToRootPackages(elem);
		}
	}
	@Override
	public void renameAll(Set<OJPathName> match,String suffix){
		Set<OJPackage> rootpackages = getRootPackages();
		for(OJPackage p:rootpackages){
			p.renameAll(match, suffix);
		}
	}
	public OJPackage findOrCreatePackage(OJPathName packageName){
		OJPackage parent = findLocalPackage(packageName.getFirst());
		if(parent == null){
			parent = new OJPackage(packageName.getFirst());
			addToRootPackages(parent);
		}
		OJPackage child = parent;
		Iterator<String> iter = packageName.getNames().subList(1, packageName.getNames().size()).iterator();
		while(iter.hasNext()){
			String name = iter.next();
			child = (OJPackage) parent.findPackage(new OJPathName(name));
			if(child == null){
				child = new OJPackage(name);
				parent.addToSubpackages(child);
			}
			parent = child;
		}
		return child;
	}
}
