package org.test.restlet;

import java.io.IOException;

import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

public class FirstClient {
	public static void main(String[] args) throws ResourceException, IOException {
		// Create the client resource
		ClientResource resource = new ClientResource("http://www.restlet.org");
		// Customize the referrer property
		resource.setReferrerRef("http://www.mysite.org");
		// Write the response entity on the console
		resource.get().write(System.out);
	}
}
