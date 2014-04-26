package org.umlg.tests.root;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.model.Umlgtest;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2014/04/23
 * Time: 8:17 PM
 */
public class TestRootMethods extends BaseLocalDbTest {

    @Test
    public void testUmlgGraphGetUmlgApplicationNode() {
        Assert.assertNotNull(UMLG.get().getUmlgApplicationNode());
        Assert.assertTrue(UMLG.get().getUmlgApplicationNode() instanceof Umlgtest);
    }

    @Test
    public void testMetaGetters() {
        Assert.assertNotNull(Umlgtest.INSTANCE.getBaseClassUmlg());
        Assert.assertNotNull(Umlgtest.INSTANCE.getAMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getAccountMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getAngelMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getAssociationClass1Meta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getAssociationClass2Meta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getAssociationClass3Meta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getAssociationClass4Meta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getAssociationClass5Meta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getAssociationToSelfMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getBMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getBagMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getBagRootMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getBagTestMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getBaseClassUmlgMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getBaseRootMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getBipedMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getBoatMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getBscMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getBtsMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getCMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getCarMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getCellMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getChildMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getClassQueryMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getCompanyMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getConstraintChild1Meta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getConstraintChild2Meta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getConstraintRootMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getCorporateAccountMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getCreatureMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getDMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getDataTypeEntityMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getDemonMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getDevil1Meta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getDevil2Meta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getDevil3Meta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getDreamMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getEMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getElmMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getFMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getFWomenMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getFakeRootFolderMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getFantasyMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getFingerMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getFolderMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getFolderXMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getFootMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getGMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getGodMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getHMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getHandMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getHorseMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getInstanceQueryMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getLegalEntityMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getLevel1Meta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getLevel2Meta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getLevel3Meta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getMamalMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getMany1Meta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getMany2Meta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getManyAAMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getManyAMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getManyBMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getMiddleRootMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getNatureMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getNevil1Meta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getNevil2Meta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getNevil3Meta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getNightmareMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getNonNavigableManyMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getNonNavigableOneMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getOakMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getOneOneMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getOneTwoMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getOrderedSetRootMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getOrderedSetTestMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getParent1Meta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getParent2Meta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getPersonMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getPersonalAccountMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getPhantomMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getQuadpedMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getRealRootFolderMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getReinsMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getRingMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getRoot1Meta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getRoot2Meta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getRootQueryMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getSequenceList2Meta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getSequenceRootMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getSequenceTest1Meta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getSequenceTestAgain1Meta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getSequenceTestAgain2Meta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getSequenceTestListManyMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getSequenceTestListOneMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getSequenceTestOrderedSetMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getSetRootMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getSetTest1Meta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getSetTestMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getSpaceMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getSpaceTimeMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getSpecialCreatureMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getSpookMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getSteeringControlMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getTagMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getTestEmbeddedMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getTestOrderedEnumerationMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getTillerMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getTimeMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getTopRootMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getTreeMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getUniverseMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getWillowMeta());
        Assert.assertNotNull(Umlgtest.INSTANCE.getWorldMeta());
    }
}
