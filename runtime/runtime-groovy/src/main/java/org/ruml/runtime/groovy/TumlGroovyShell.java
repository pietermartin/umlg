package org.ruml.runtime.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import com.tinkerpop.blueprints.Vertex;

public class TumlGroovyShell {

	public static void main(String[] args) {
		Binding binding = new Binding();
		binding.setVariable("foo", new Integer(2));
		GroovyShell shell = new GroovyShell(binding);
		Object value = shell.evaluate("println 'Hello World!'; x = 123; return foo * 10");
		assert value.equals(new Integer(20));
		assert binding.getVariable("x").equals(new Integer(123));
		
		shell.evaluate("public class Hello {public String sayIt(){return \"wtf this is almost kewl!!!\";}}; def hello = new Hello(); println hello.sayIt();");
		
	}
	
	public static Object executeQuery(String javaClassAsString, Vertex v) {
		Binding binding = new Binding();
		binding.setVariable("v", v);
		GroovyShell shell = new GroovyShell(binding);
		Object value = shell.evaluate(javaClassAsString + "; def oclQuery = new OclQuery(v); return oclQuery.execute();");
		return value;
	}
	
	/**
	 * This is for static ocl, i.e. allInstances
	 * @param javaClassAsString
	 * @return
	 */
	public static Object executeQuery(String javaClassAsString) {
		Binding binding = new Binding();
		GroovyShell shell = new GroovyShell(binding);
		Object value = shell.evaluate(javaClassAsString + "; def oclQuery = new OclQuery(); return oclQuery.execute();");
		return value;
	}

}
