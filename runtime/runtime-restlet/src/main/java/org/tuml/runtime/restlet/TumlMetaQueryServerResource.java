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
public interface TumlMetaQueryServerResource {

    @Delete("json")
    public Representation delete(Representation entity);

    @Get("json")
    public Representation get();

    @Post("json")
    public Representation post(Representation entity);

    @Put("json")
    public Representation put(Representation entity);

}
