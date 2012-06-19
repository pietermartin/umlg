package org.tuml.ocl;

import tudresden.ocl20.pivot.metamodels.uml2.UML2MetamodelPlugin;
import tudresden.ocl20.pivot.model.IModelProvider;
import tudresden.ocl20.pivot.model.metamodel.IMetamodel;

/**
 * Instead of using the extension point, meta-models can be added directly by
 * implementing {@link IMetamodel}.
 * 
 * @author Michael Thiele
 * 
 */
public class UMLMetamodel implements IMetamodel {

	private IModelProvider modelProvider = new StandaloneUML2ModelProvider();

	/*
	 * (non-Javadoc)
	 * @see tudresden.ocl20.pivot.modelbus.metamodel.IMetamodel#getId()
	 */
	public String getId() {

		return UML2MetamodelPlugin.ID;
	}

	/*
	 * (non-Javadoc)
	 * @see tudresden.ocl20.pivot.modelbus.metamodel.IMetamodel#getModelProvider()
	 */
	public IModelProvider getModelProvider() {

		return modelProvider;
	}

	/*
	 * (non-Javadoc)
	 * @see tudresden.ocl20.pivot.modelbus.metamodel.IMetamodel#getName()
	 */
	public String getName() {

		return "UML";
	}

}
