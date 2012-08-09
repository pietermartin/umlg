package restlet;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;

public interface RootServerResource {
	@Get(	"json")
	public Representation get();


}