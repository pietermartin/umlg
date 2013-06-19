package org.umlg.runtime.domain.activity;

import org.umlg.runtime.collection.TumlRuntimeProperty;
import org.umlg.runtime.domain.DataTypeEnum;

public class TumlRuntimePropertyImpl implements TumlRuntimeProperty {

	public TumlRuntimePropertyImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isControllingSide() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isComposite() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOneToOne() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOneToMany() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isManyToOne() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isManyToMany() {
		// TODO Auto-generated method stub
		return false;
	}

    @Override
    public int getInverseUpper() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
	public int getUpper() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getLower() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isValid(int elementCount) {
		return (getUpper() == -1 || elementCount <= getUpper()) && elementCount >= getLower();
	}

    @Override
    public boolean isAssociationClass() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
	public boolean isOnePrimitive() {
		return false;
	}

	@Override
	public boolean isQualified() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInverseQualified() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOrdered() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInverseOrdered() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUnique() {
		// TODO Auto-generated method stub
		return false;
	}

    @Override
    public boolean isInverseUnique() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
	public String toJson() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isManyPrimitive() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInverseComposite() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOneEnumeration() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isManyEnumeration() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DataTypeEnum getDataTypeEnum() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getQualifiedName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInverseQualifiedName() {
		// TODO Auto-generated method stub
		return null;
	}

}
