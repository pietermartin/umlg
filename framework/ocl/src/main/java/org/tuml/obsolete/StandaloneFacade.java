package org.tuml.obsolete;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.eclipse.core.runtime.internal.adaptor.IModel;
import org.eclipse.emf.common.util.URI;

/**
 * <p>
 * The <code>StandaloneFacade</code> can be used by clients that want to use
 * DresdenOCL without Eclipse.
 * </p>
 * <p>
 * <strong>Attention:</strong> Before calling any operation on the facade, make
 * sure it is initialized by calling {@link #initialize(URL)}! Any operation
 * call on an uninitialized facade will result in an exception.
 * </p>
 * <p>
 * The facade supports the following tasks:
 * <ul>
 * <li>configure the logging for the different components of DresdenOCL by
 * calling {@link #initialize(URL)} with an {@link URL} pointing to
 * log4j.properties</li>
 * <li>load models ({@link #loadUMLModel(File)}, {@link #loadEcoreModel(File)})</li>
 * <li>parse OCL constraints that are listed in a file (
 * {@link #parseOclConstraints(IModel, File)},
 * {@link #parseOclConstraints(IModel, URI)})</li>
 * <li>load model instances ({@link #loadJavaModelInstance(IModel, File)},
 * {@link #loadEcoreModelInstance(IModel, File)})</li>
 * <li>interprete a given list of constraints on a model instance (
 * {@link #interpretEverything(IModelInstance, List)})</li>
 * </ul>
 * </p>
 * 
 * @author Michael Thiele
 * 
 */
public class StandaloneFacade {

//	/** singleton instance */
//	private static StandaloneFacade instance;
//	private boolean initialized = false;
//	private boolean registeredUMLMetamodel = false;
//	private IMetamodelRegistry standaloneMetamodelRegistry = new StandaloneMetamodelRegistry();
//	private IOcl2Java javaCodeGenerator;
//	private IModel model;
//	
//	/**
//	 * Returns the single instance of the {@link StandaloneFacade}.
//	 */
//	public static StandaloneFacade INSTANCE = instance();
//
//	private static StandaloneFacade instance() {
//
//		if (instance == null) {
//			instance = new StandaloneFacade();
//		}
//		return instance;
//	}
//
//	/** private constructor for Singleton pattern */
//	private StandaloneFacade() {
//
//	}
//
//	/**
//	 * <p>
//	 * <strong>Call this method before calling anything else on the
//	 * facade.</strong> It will initialize DresdenOCL and the facade.
//	 * </p>
//	 * <p>
//	 * If you want to get log messages of certain parts of DresdenOCL, you can
//	 * give the location of a log4j.properties file.
//	 * </p>
//	 * 
//	 * @param loggerPropertiesUrl
//	 *            the {@link URL} of log4j.properties or <code>null</code> if
//	 *            you don't want to log
//	 * @throws TemplateException
//	 */
//	public void initialize(URL loggerPropertiesUrl) throws TemplateException {
//
//		if (!initialized) {
//			/*
//			 * This little hack allows us to access these plug-ins even if there
//			 * is no Eclipse to instantiate them.
//			 */
//			new LoggingPlugin(loggerPropertiesUrl);
//			new EssentialOclPlugin();
//			new TemplatePlugin();
//			new TransformationPlugin();
//
//			EssentialOclPlugin
//					.setOclLibraryProvider(new StandaloneOclLibraryProvider(
//							StandaloneFacade.class
//									.getResourceAsStream("/oclstandardlibrary.types")));
//
//			// only needed for code generation
//			final StringTemplateEngine stringTemplateEngine = new StringTemplateEngine();
//
//			ITemplateEngineRegistry templateEngineRegistry = new StandaloneTemplateEngineRegistry();
//			templateEngineRegistry.addTemplateEngine(stringTemplateEngine);
//
//			ITemplateGroupRegistry templateGroupRegistry = new StandaloneTemplateGroupRegistry();
//			templateGroupRegistry.addTemplateGroup(new TemplateGroup(
//					stringTemplateEngine.getDisplayName(), null,
//					stringTemplateEngine));
//
//			TemplatePlugin.setTempateEngineRegistry(templateEngineRegistry);
//			TemplatePlugin.setTempateGroupRegistry(templateGroupRegistry);
//
//			ITransformationRegistry transformationRegistry = new StandaloneTransformationRegistry();
//			TransformationPlugin
//					.setTransformationRegistry(transformationRegistry);
//
//			// needed for parsing (static semantics analysis)
//			OclReferenceResolveHelperProvider
//					.setOclReferenceResolveHelper(new OclReferenceResolveHelper());
//
//			initialized = true;
//		}
//	}
//	
//	/**
//	 * Parses the OCL constraints in a given file and returns a list of
//	 * {@link Constraint}s that can be used for interpretation.
//	 * 
//	 * @param model
//	 *            the model the constraints are defined on
//	 * @param oclFile
//	 *            the file containing the OCL constraints
//	 * @return a lit of {@link Constraint}s
//	 * @throws IOException
//	 * @throws ParseException
//	 *             if something went wrong during parsing
//	 */
//	public List<Constraint> parseOclConstraints(IModel model, File oclFile)
//			throws IOException, ParseException {
//
//		if (!oclFile.exists())
//			throw new FileNotFoundException("Cannot find file "
//					+ oclFile.getCanonicalPath() + ".");
//
//		return Ocl22Parser.INSTANCE.doParse(model,
//				URI.createFileURI(oclFile.getCanonicalPath()));
//	}
//
//	/**
//	 * Parses the OCL constraints in a given URI and returns a list of
//	 * {@link Constraint}s that can be used for interpretation.
//	 * 
//	 * @param model
//	 *            the model the constraints are defined on
//	 * @param uri
//	 *            the {@link URI} of the OCL constraints
//	 * @return a lit of {@link Constraint}s
//	 * @throws ParseException
//	 *             if something went wrong during parsing
//	 */
//	public List<Constraint> parseOclConstraints(IModel model, URI uri)
//			throws ParseException {
//
//		return Ocl22Parser.INSTANCE.doParse(model, uri);
//	}
//
//
//	public IMetamodelRegistry getStandaloneMetamodelRegistry() {
//
//		return standaloneMetamodelRegistry;
//	}
//
//	/**
//	 * Loads a UML model from the given file.
//	 * 
//	 * @param modelFile
//	 *            the UML model
//	 * @param umlResources
//	 *            points to the jar file of the plug-in
//	 *            <code>org.eclipse.uml2.uml.resources</code>; this is necessary
//	 *            in order to use primitive types like String and Integer in UML
//	 *            models
//	 * @return an adapted UML model that can be used for parsing OCL constraints
//	 *         and loading model instances
//	 * @throws ModelAccessException
//	 *             if something went wrong while loading the UML model
//	 */
//	public IModel loadUMLModel(File modelFile, /*File umlResources*/ URI pluginURI)
//			throws ModelAccessException {
//
//		checkInitialized();
//
////		if (umlResources == null)
////			throw new IllegalArgumentException(
////					"Cannot laod an UML model with umlResources == null; umlResources has to point to the jar file of the plugin org.eclipse.uml2.uml.resources.");
//
//		if (!registeredUMLMetamodel) {
//			EPackage.Registry.INSTANCE.put(UMLPackage.eNS_URI,
//					UMLPackage.eINSTANCE);
//			Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap()
//					.put(UMLResource.FILE_EXTENSION,
//							UMLResourceFactoryImpl.INSTANCE);
//
////			URI pluginURI = URI.createURI("jar:file:"
////					+ umlResources.getAbsolutePath() + "!/");
//			URIConverter.URI_MAP.put(URI
//					.createURI(UMLResource.LIBRARIES_PATHMAP), pluginURI
//					.appendSegment("libraries").appendSegment(""));
//			URIConverter.URI_MAP.put(URI
//					.createURI(UMLResource.METAMODELS_PATHMAP), pluginURI
//					.appendSegment("metamodels").appendSegment(""));
//			URIConverter.URI_MAP.put(URI
//					.createURI(UMLResource.PROFILES_PATHMAP), pluginURI
//					.appendSegment("profiles").appendSegment(""));
//
//			registeredUMLMetamodel = true;
//		}
//
//		IMetamodel umlMetamodel = new UMLMetamodel();
//		standaloneMetamodelRegistry.addMetamodel(umlMetamodel);
//		this.model = umlMetamodel.getModelProvider().getModel(modelFile);
//		return this.model;
//	}
//	
//	public IModel getModel() {
//		return this.model;
//	}
//
//	/**
//	 * Generates Java code for the given constraints. Please not that this
//	 * method will only generate the Java code for a given OCL expression and
//	 * will not generate code required to instrument such code within a
//	 * constrained Java class. If you want to generated instrumentation code,
//	 * use the method
//	 * {@link StandaloneFacade#generateAspectJCode(List, IOcl2JavaSettings)}
//	 * instead.
//	 * 
//	 * @param constraints
//	 *            the constraints for which Java code should be created
//	 * @param settings
//	 *            the {@link IOcl2JavaSettings} containing at least a directory
//	 *            into which the code should be generated
//	 * @throws Ocl2CodeException
//	 *             if the {@link IOcl2JavaSettings} were not set properly or
//	 *             something went wrong during code generation
//	 * @return A {@link List} of {@link String} containing the generated Java
//	 *         code.
//	 */
//	public List<String> generateJavaCode(List<Constraint> constraints,
//			IOcl2JavaSettings settings) throws Ocl2CodeException {
//
//		if (settings == null)
//			throw new Ocl2CodeException("The IOcl2JavaSettings cannot be null.");
//		// no else.
//
//		settings.setSaveCode(false);
//
//		if (javaCodeGenerator == null) {
//			javaCodeGenerator = Ocl2JavaFactory.getInstance()
//					.createJavaCodeGenerator();
//		}
//		// no else.
//
//		javaCodeGenerator.resetEnvironment();
//		javaCodeGenerator.setSettings(settings);
//		return javaCodeGenerator.transformFragmentCode(constraints);
//	}
//
//	private void checkInitialized() {
//
//		if (!initialized)
//			throw new IllegalStateException(
//					"The StandaloneFacade needs to be initialised. Call StandaloneFacade.INSTANCE.initalize(URL) first.");
//
//	}

}
