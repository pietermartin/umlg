package org.tuml.ocl;


public class TumlOcl2 {

//	final static File rlModel = new File("src/main/resources/model/royalsandloyals.uml");
//	final static File rlOclConstraints = new File("src/main/resources/constraints/rl_allConstraints.ocl");
//	
//	public static void main(String[] args) throws MalformedURLException, TemplateException {
//		TumlOcl2 tumlOcl = new TumlOcl2();
//		tumlOcl.test();
//	}
//	
//	public void test() throws MalformedURLException, TemplateException {
//		StandaloneFacade.INSTANCE.initialize(new URL("file:" + new File("src/main/resources/log4j.properties").getAbsolutePath()));
//		try {
//			URLClassLoader loader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
//			URI uri = URI.createURI(ModelLoader.findLocation(loader, true, "org/eclipse/uml2/uml/resources", "org.eclipse.uml2.uml.resources"));
//			IModel model = StandaloneFacade.INSTANCE.loadUMLModel(rlModel, uri);
//			List<Constraint> constraintList = StandaloneFacade.INSTANCE.parseOclConstraints(model, rlOclConstraints);
//			IOcl2JavaSettings settings = Ocl2JavaFactory.getInstance().createJavaCodeGeneratorSettings();
//			settings.setGettersForPropertyCallsEnabled(true);
//			settings.setSourceDirectory("src/main/generated-ocl");
//			
//			List<String> result = StandaloneFacade.INSTANCE.generateJavaCode(constraintList, settings);
//			int i  = 0;
//			for (String string : result) {
//				System.out.println(constraintList.get(i++));
//				System.out.println(string);
//				System.out.println("========");
//			}
//			System.out.println("Finished code generation.");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//   	}
	
}
