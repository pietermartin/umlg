package org.tuml.runtime.restlet;

import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;

/**
 * Date: 2012/12/26
 * Time: 6:02 PM
 */
public interface TumlTransactionServerResource {

    public static final String COMMITTED = "committed";
    public static final String COMMIT = "commit";
    public static final String ROLLED_BACK = "rolled back";

    @Delete("validation")
    public Representation delete(Representation entity);

    @Post("validation")
    public Representation post(Representation entity);

    @Put("validation")
    public Representation put(Representation entity);

}
