package org.tuml;

import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.domain.TumlNode;

public interface Interface1 extends TumlNode {
	public void addToInterface2(Interface2 interface2);
	
	public void addToInterface2(TinkerSet<Interface2> interface2);
	
	public void addToName(String name);
	
	public void clearInterface2();
	
	public void clearName();
	
	public TinkerSet<Interface2> getInterface2();
	
	public String getName();
	
	public void removeFromInterface2(Interface2 interface2);
	
	public void removeFromInterface2(TinkerSet<Interface2> interface2);
	
	public void removeFromName(String name);
	
	public void removeFromName(TinkerSet<String> name);
	
	public void setInterface2(TinkerSet<Interface2> interface2);
	
	public void setName(String name);

	public enum Interface1RuntimePropertyEnum implements TumlRuntimeProperty {
		name(true,true,false,"basicmodel__org__tuml__Interface1__name",false,false,true,false,1,1,false,false,false,false,true),
		interface2(false,true,true,"A_<interface1>_<interface2>",false,true,false,false,-1,0,false,false,false,false,true);
		private boolean onePrimitive;
		private boolean controllingSide;
		private boolean composite;
		private String label;
		private boolean oneToOne;
		private boolean oneToMany;
		private boolean manyToOne;
		private boolean manyToMany;
		private int upper;
		private int lower;
		private boolean qualified;
		private boolean inverseQualified;
		private boolean ordered;
		private boolean inverseOrdered;
		private boolean unique;
		/**
		 * constructor for Interface1RuntimePropertyEnum
		 * 
		 * @param onePrimitive 
		 * @param controllingSide 
		 * @param composite 
		 * @param label 
		 * @param oneToOne 
		 * @param oneToMany 
		 * @param manyToOne 
		 * @param manyToMany 
		 * @param upper 
		 * @param lower 
		 * @param qualified 
		 * @param inverseQualified 
		 * @param ordered 
		 * @param inverseOrdered 
		 * @param unique 
		 */
		private Interface1RuntimePropertyEnum(boolean onePrimitive, boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower, boolean qualified, boolean inverseQualified, boolean ordered, boolean inverseOrdered, boolean unique) {
			this.onePrimitive = onePrimitive;
			this.controllingSide = controllingSide;
			this.composite = composite;
			this.label = label;
			this.oneToOne = oneToOne;
			this.oneToMany = oneToMany;
			this.manyToOne = manyToOne;
			this.manyToMany = manyToMany;
			this.upper = upper;
			this.lower = lower;
			this.qualified = qualified;
			this.inverseQualified = inverseQualified;
			this.ordered = ordered;
			this.inverseOrdered = inverseOrdered;
			this.unique = unique;
		}
	
		static public Interface1RuntimePropertyEnum fromLabel(String label) {
			if ( name.getLabel().equals(label) ) {
				return name;
			}
			if ( interface2.getLabel().equals(label) ) {
				return interface2;
			}
			return null;
		}
		
		public String getLabel() {
			return this.label;
		}
		
		public int getLower() {
			return this.lower;
		}
		
		public int getUpper() {
			return this.upper;
		}
		
		public boolean isComposite() {
			return this.composite;
		}
		
		public boolean isControllingSide() {
			return this.controllingSide;
		}
		
		public boolean isInverseOrdered() {
			return this.inverseOrdered;
		}
		
		public boolean isInverseQualified() {
			return this.inverseQualified;
		}
		
		public boolean isManyToMany() {
			return this.manyToMany;
		}
		
		public boolean isManyToOne() {
			return this.manyToOne;
		}
		
		public boolean isOnePrimitive() {
			return this.onePrimitive;
		}
		
		public boolean isOneToMany() {
			return this.oneToMany;
		}
		
		public boolean isOneToOne() {
			return this.oneToOne;
		}
		
		public boolean isOrdered() {
			return this.ordered;
		}
		
		public boolean isQualified() {
			return this.qualified;
		}
		
		public boolean isUnique() {
			return this.unique;
		}
		
		@Override
		public boolean isValid(int elementCount) {
			if ( isQualified() ) {
				return elementCount >= getLower();
			} else {
				return (getUpper() == -1 || elementCount <= getUpper()) && elementCount >= getLower();
			}
		}
	
	}
}