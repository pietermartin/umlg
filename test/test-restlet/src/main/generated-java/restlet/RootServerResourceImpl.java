package restlet;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.tuml.test.Human.HumanRuntimePropertyEnum;

public class RootServerResourceImpl extends ServerResource implements RootServerResource {


	/**
	 * default constructor for RootServerResourceImpl
	 */
	public RootServerResourceImpl() {
		setNegotiated(false);
	}

	@Override
	public Representation get() throws ResourceException {
		StringBuilder json = new StringBuilder();
		json.append("[");
		json.append(HumanRuntimePropertyEnum.asJson());
		json.append("]");
		return new JsonRepresentation(json.toString());
	}


}