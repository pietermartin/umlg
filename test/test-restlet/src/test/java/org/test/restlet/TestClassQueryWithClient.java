package org.test.restlet;

import org.codehaus.jettison.json.JSONException;
import org.eclipse.uml2.uml.Model;
import org.junit.*;
import org.restlet.Client;
import org.restlet.Component;
import org.restlet.Context;
import org.restlet.data.Protocol;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.tuml.framework.ModelLoadedEvent;
import org.tuml.restandjson.RestAndJsonComponent;
import org.tuml.restlet.test.BaseRestletTest;
import org.tuml.runtime.restlet.TumlMetaQueryServerResource;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Date: 2012/12/26
 * Time: 6:14 PM
 */
public class TestClassQueryWithClient extends BaseRestletTest {

    private static Logger logger = Logger.getLogger(TestClassQueryWithClient.class.getName());

    @Override
    protected Component instantiateComponent() {
        return new RestAndJsonComponent();
    }

    @org.junit.Test
    public void testClassQuery() throws ResourceException, JSONException, IOException {
        logger.info("testClassQuery");
        Client client = new Client(new Context(), Protocol.HTTP);
        ClientResource service = new ClientResource("http://localhost:8111/");
        service.setNext(client);
        TumlMetaQueryServerResource rootServerResource = service.getChild("/restAndJson/classquery/2/query", TumlMetaQueryServerResource.class);
        String s = rootServerResource.get().getText();
        System.out.println("halo == " + s);
        System.out.println("Hi there");
    }

}
