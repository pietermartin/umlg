package org.tuml.obsolete;


/**
 * <p>
 * Implementation of the {@link IModelProvider} interface for UML2 models. This
 * implementation will create an {@link UML2Model} instance.
 * </p>
 * 
 * @author Michael Thiele
 */
public class StandaloneUML2ModelProvider /*extends AbstractModelProvider
		implements IModelProvider*/ {

//	/**
//	 * <p>
//	 * The {@link Logger} for this class.
//	 * </p>
//	 */
//	private static final Logger LOGGER =
//			UML2MetamodelPlugin.getLogger(StandaloneUML2ModelProvider.class);
//
//	/** The resourceSet. */
//	protected ResourceSet resourceSet;
//
//	/**
//	 * @see tudresden.ocl20.pivot.model.IModelProvider#getModel(java.net.URL)
//	 * 
//	 * @generated NOT
//	 */
//	public IModel getModel(URL modelURL) throws ModelAccessException {
//
//		/* Eventually debug the entry of this method. */
//		if (LOGGER.isDebugEnabled()) {
//			LOGGER.debug("getModel(modelURL=" + modelURL + ") - enter");
//		}
//		// no else.
//
//		IModel result;
//
//		URI modelURI;
//		Resource resource;
//
//		result = null;
//
//		/* Try to create a URI from the given URL. */
//		try {
//			modelURI = URI.createURI(modelURL.toString());
//		}
//
//		catch (IllegalArgumentException e) {
//			throw new ModelAccessException("Invalid URL: " + modelURL, e);
//		}
//
//		/* Get the resource of the given URI. */
//		resource = getResourceSet().getResource(modelURI, false);
//
//		/* Check if the resource exists. */
//		if (resource == null) {
//			/* We only want to create the resource, not load it. */
//			resource = getResourceSet().createResource(modelURI);
//		}
//		// no else.
//
//		result =
//				new UML2Model(getResourceSet().getResource(modelURI, false),
//						StandaloneFacade.INSTANCE.getStandaloneMetamodelRegistry()
//								.getMetamodel(UML2MetamodelPlugin.ID));
//
//		/* Eventually debug the exit of this method. */
//		if (LOGGER.isDebugEnabled()) {
//			LOGGER.debug("getModel() - exit - return value=" + result); //$NON-NLS-1$
//		}
//		// no else.
//
//		return result;
//	}
//
//	/**
//	 * <p>
//	 * A helper method that lazily creates a resource set.
//	 * </p>
//	 * 
//	 * @return The created {@link ResourceSet}.
//	 */
//	protected ResourceSet getResourceSet() {
//
//		/* Eventually initialize the resource set. */
//		if (this.resourceSet == null) {
//			this.resourceSet = new ResourceSetImpl();
//		}
//		// no else.
//
//		return this.resourceSet;
//	}
}
