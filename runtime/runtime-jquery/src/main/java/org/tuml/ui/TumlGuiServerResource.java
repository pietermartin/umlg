package org.tuml.ui;

import java.util.HashMap;
import java.util.Map;

import org.restlet.data.LocalReference;
import org.restlet.data.MediaType;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

public class TumlGuiServerResource extends ServerResource {

	public TumlGuiServerResource() {
		setNegotiated(false);
	}

	@Override
	protected Representation get() throws ResourceException {
		System.out.println("getHostRef = " + getHostRef());
		System.out.println("getLocationRef = " + getLocationRef());
		System.out.println("getReference = " + getReference());
		System.out.println("getReferrerRef = " + getReferrerRef());
		System.out.println("getOriginalRef = " + getOriginalRef());
		System.out.println("getRootRef = " + getRootRef());
		System.out.println("getResourceRef = " + getRequest().getResourceRef());
		Map<String, Object> requestAttr = getRequestAttributes();
		for (String requestKey : requestAttr.keySet()) {
			System.out.println(requestKey + " : " + requestAttr.get(requestKey));
		}
		
		Map<String, Object> dataModel = new HashMap<String, Object>();
		String uri = getOriginalRef().toString().replace(getHostRef().toString(), "").replace("/ui", "");

		//TODO work this hardcoding out
		dataModel.put("app", new App().setRootUrl("restAndJson").setUri(uri));
		
		Representation tumlUiFtl = new ClientResource(LocalReference.createClapReference(getClass().getPackage())	+ "/tumlui.ftl").get();
		return new TemplateRepresentation(tumlUiFtl, dataModel, MediaType.TEXT_HTML);
	}

}
