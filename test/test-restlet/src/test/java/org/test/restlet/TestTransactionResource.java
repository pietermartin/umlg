package org.test.restlet;

import org.codehaus.jettison.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.restlet.Client;
import org.restlet.Component;
import org.restlet.Context;
import org.restlet.data.Protocol;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.tuml.restandjson.RestAndJsonComponent;
import org.tuml.restlet.test.BaseRestletTest;
import org.tuml.runtime.restlet.TumlTransactionServerResource;

import java.io.IOException;

public class TestTransactionResource extends BaseRestletTest {

    @Test
    public void testTransactionResourceCommit() throws ResourceException, JSONException, IOException {
        Client client = new Client(new Context(), Protocol.HTTP);
        ClientResource service = new ClientResource("http://localhost:8111/");
        service.setNext(client);
        TumlTransactionServerResource transactionServerResource = service.getChild("/restAndJson/transaction", TumlTransactionServerResource.class);
        Representation representation = transactionServerResource.post(new StringRepresentation(""));
        String uid = representation.getText();
        representation = transactionServerResource.put(new StringRepresentation("{\"" + TumlTransactionServerResource.COMMIT + "\": true}"));
        String result = representation.getText();
        Assert.assertEquals(TumlTransactionServerResource.COMMITTED, result);
    }

    @Test
    public void testTransactionResourceRollback() throws ResourceException, JSONException, IOException {
        Client client = new Client(new Context(), Protocol.HTTP);
        ClientResource service = new ClientResource("http://localhost:8111/");
        service.setNext(client);
        TumlTransactionServerResource transactionServerResource = service.getChild("/restAndJson/transaction", TumlTransactionServerResource.class);
        Representation representation = transactionServerResource.post(new StringRepresentation(""));
        String uid = representation.getText();
        representation = transactionServerResource.put(new StringRepresentation("{\"" + TumlTransactionServerResource.COMMIT + "\": false}"));
        String result = representation.getText();
        Assert.assertEquals(TumlTransactionServerResource.ROLLED_BACK, result);
    }


    @Override
    protected Component instantiateComponent() {
        return new RestAndJsonComponent();
    }
}
