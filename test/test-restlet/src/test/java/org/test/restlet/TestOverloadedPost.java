package org.test.restlet;

import org.junit.Assert;
import org.junit.Test;
import org.restlet.Client;
import org.restlet.Component;
import org.restlet.Context;
import org.restlet.data.Protocol;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.tuml.restandjson.RestAndJsonComponent;
import org.tuml.restlet.RootServerResource;
import org.tuml.restlet.client.json.*;
import org.tuml.restlet.test.BaseRestletTest;
import org.tuml.runtime.restlet.TumlTransactionServerResource;
import org.tuml.test.restlet.Human_human_hand_Hand_OverloadedPost_ServerResource;
import org.tuml.test.restlet.Human_human_hand_Hand_ServerResource;
import org.tuml.test.restlet.HumansServerResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Date: 2013/02/06
 * Time: 9:29 PM
 */
public class TestOverloadedPost extends BaseRestletTest {

    @Test
    public void testTransactionalPost() throws IOException {
        Client client = new Client(new Context(), Protocol.HTTP);
        ClientResource service = new ClientResource("http://localhost:8111/");
        service.setNext(client);

        RootServerResource rootServerResource = service.getChild("/restAndJson", RootServerResource.class);
        Representation rootRepresentation = rootServerResource.get();
        String rootAsJson = rootRepresentation.getText();

        Assert.assertTrue(rootAsJson != null);

        RootJson rootJson = new RootJson(rootAsJson);
        Assert.assertEquals(1, rootJson.getDataAndMetas().size());
        DataJson dataJson = rootJson.getDataAndMetas().get(0).getDataJson();
        MetaJson metaJson = rootJson.getDataAndMetas().get(0).getMetaJson();

        Assert.assertEquals(1, dataJson.getObjects().size());
        Assert.assertTrue(metaJson.getFrom() == null);
        MetaToFrom metaToFrom_to = metaJson.getTo();
        Assert.assertNotNull(metaToFrom_to);
        Assert.assertEquals("restAndJson", metaJson.getQualifiedName());

        Assert.assertEquals("Root", metaToFrom_to.getName());
        Assert.assertEquals("/restAndJson", metaToFrom_to.getUri());
        List<MetaPropertyJson> metaPropertyJsons = metaToFrom_to.getProperties();
        //Includes one to the model or self
        Assert.assertEquals(3, metaPropertyJsons.size());

        MetaPropertyJson metaHuman = null;
        //Go fetch the humans
        for (MetaPropertyJson metaPropertyJson : metaPropertyJsons) {
            if (metaPropertyJson.getQualifiedName().equals("restAndJson::org::tuml::test::Human")) {
                metaHuman = metaPropertyJson;
                break;
            }
        }

        String getHumansTumlUri = metaHuman.getTumlUri();
        HumansServerResource humanServerResource = service.getChild(getHumansTumlUri, HumansServerResource.class);
        Representation humansRepresentation = humanServerResource.get();
        String humansAsJson = humansRepresentation.getText();
        RootJson humansJson = new RootJson(humansAsJson);

        DataJson humanDataJson = humansJson.getDataAndMetas().get(0).getDataJson();
        MetaJson humanMetaJson = humansJson.getDataAndMetas().get(0).getMetaJson();
        ObjectJson humanJson = humanDataJson.getObjects().get(0);

        MetaToFrom humanMetaTo = humanMetaJson.getTo();
        MetaPropertyJson metaHand = null;
        //Go fetch the humans
        for (MetaPropertyJson metaPropertyJson : humanMetaTo.getProperties()) {
            if (metaPropertyJson.getQualifiedName().equals("restAndJson::org::tuml::test::Human::hand")) {
                metaHand = metaPropertyJson;
                break;
            }
        }
        Assert.assertNotNull(metaHand);

        //Get the human's id
        PropertyJson humanId = humanJson.get("id");

        String handUri = metaHand.getTumlUri().replace("{humanId}", Integer.toString((Integer) humanId.getValue()));
        Human_human_hand_Hand_ServerResource humanHumanHandHandServerResource = service.getChild(handUri, Human_human_hand_Hand_ServerResource.class);
        DataJson hands = new RootJson(humanHumanHandHandServerResource.get().getText()).getDataAndMetas().get(0).getDataJson();
        Assert.assertEquals(2, hands.count());

        //Create a new hand
        PropertyJson handNamePropertyJson = new PropertyJson("name", "SpecialLeftHand");
        PropertyJson handTestUnlimitedNaturalPropertyJson = new PropertyJson("testUnlimitedNatural", 1000000000000L);
        PropertyJson handTestNumberPropertyJson = new PropertyJson("testNumber", 54);
        ObjectJson newHandObjectJson = new ObjectJson(Arrays.asList(handNamePropertyJson, handTestUnlimitedNaturalPropertyJson, handTestNumberPropertyJson));

        List<ObjectJson> objectJsonList = new ArrayList<ObjectJson>();
        objectJsonList.add(newHandObjectJson);
        DataJson newHandsDataJson = DataJson.createDataJsonFromObjectJsons(objectJsonList);
        newHandsDataJson.moveToInsert();

        //Delete a hand
        ObjectJson handToDeleteObjectJson = hands.getObjects().get(0);
        String deletedHandName = (String) handToDeleteObjectJson.get("name").getValue();
        newHandsDataJson.setObjects(Arrays.asList(handToDeleteObjectJson));
        newHandsDataJson.moveToDelete();

        String handOverloadedPostUri = metaHand.getTumlOverloadedPostUri().replace("{humanId}", Integer.toString((Integer) humanId.getValue()));
        Human_human_hand_Hand_OverloadedPost_ServerResource human_human_hand_hand_overloadedPost_serverResource = service.getChild(handOverloadedPostUri, Human_human_hand_Hand_OverloadedPost_ServerResource.class);

        Representation overloadedPostRepresentation = human_human_hand_hand_overloadedPost_serverResource.post(new StringRepresentation(newHandsDataJson.toJson()));
        String result = overloadedPostRepresentation.getText();
        RootJson handsResult = new RootJson(result);
        DataJson resultHandsDataJson = handsResult.getDataAndMetas().get(0).getDataJson();
        Assert.assertEquals(2, resultHandsDataJson.getObjects().size());

        ObjectJson hand1 = resultHandsDataJson.getObjects().get(0);
        ObjectJson hand2 = resultHandsDataJson.getObjects().get(1);

        Assert.assertTrue(hand1.get("name").getValue().equals("SpecialLeftHand") || hand2.get("name").getValue().equals("SpecialLeftHand"));
        Assert.assertTrue(!hand1.get("name").getValue().equals(deletedHandName) && !hand2.get("name").getValue().equals(deletedHandName));

    }

    @Override
    protected Component instantiateComponent() {
        return new RestAndJsonComponent();
    }

}
