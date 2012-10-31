package org.test.restlet;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.restlet.Client;
import org.restlet.Context;
import org.restlet.data.Protocol;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.tuml.framework.ModelLoader;
import org.tuml.ocl.TumlOcl2Parser;
import org.tuml.root.Root;
import org.tuml.runtime.restlet.OclExecution_ServerResource;
import org.tuml.test.Hand;
import org.tuml.test.Human;

public class TestOclExecution {

	@BeforeClass
	public static void beforeClass() throws Exception {
		ModelLoader.loadModel(new File("src/main/model/restANDjson.uml"));
		@SuppressWarnings("unused")
		TumlOcl2Parser instance = TumlOcl2Parser.INSTANCE;
		new TumlRestletServerComponent2().start();
	}

	@AfterClass
	public static void afterClass() throws Exception {
		new TumlRestletServerComponent2().stop();
	}

	@Test
	public void testOclExecution() throws ResourceException, JSONException, IOException {
		Client client = new Client(new Context(), Protocol.HTTP);
		ClientResource service = new ClientResource("http://localhost:8111/");
		service.setNext(client);
		Human human = Root.INSTANCE.getHuman().get(0);
		Set<Hand> hands = human.getHand();
		Hand theHand = null;
		for (Hand hand : hands) {
			if (hand.getName().equals("hand1")) {
				theHand = hand;
				break;
			}
		}
		OclExecution_ServerResource oclExecutionServerResource = service.getChild("/restAndJson/oclExecution/" + human.getId()
				+ "?ocl=self.hand->select(name='hand1')", OclExecution_ServerResource.class);
		JSONObject jsonObject = new JSONObject(oclExecutionServerResource.get().getText());
		Assert.assertEquals(theHand.getId().intValue(), jsonObject.get("id"));
	}

}
