package org.umlg.tests.associationclass;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.associationclass.VirtualGroup;
import org.umlg.associationclass.VirtualGroupWorkspaceElementAC;
import org.umlg.associationclass.WorkspaceElement;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.test.BaseLocalDbTest;

/**
 * Date: 2015/02/08
 * Time: 9:43 AM
 */
public class TestAssociationClass extends BaseLocalDbTest {

    @Test
    public void testNavToAssociationClass() {
        VirtualGroup vg1  = new VirtualGroup();
        vg1.setName("vg1");

        WorkspaceElement we1 = new WorkspaceElement();
        we1.setName("we1");
        WorkspaceElement we2 = new WorkspaceElement();
        we2.setName("we2");

        VirtualGroupWorkspaceElementAC ac1 = new VirtualGroupWorkspaceElementAC();
        ac1.setName("ac1");
        VirtualGroupWorkspaceElementAC ac2 = new VirtualGroupWorkspaceElementAC();
        ac2.setName("ac2");

        vg1.addToWorkspaceelement(we1,ac1);
        vg1.addToWorkspaceelement(we2, ac2);

        UMLG.get().commit();

        vg1.reload();
        Assert.assertEquals(2, vg1.getVirtualGroupWorkspaceElementAC().size());
        //This will null pointer if its the wrong type
        for (VirtualGroupWorkspaceElementAC virtualGroupWorkspaceElementAC : vg1.getVirtualGroupWorkspaceElementAC()) {
            System.out.println(virtualGroupWorkspaceElementAC.getName());
        }

    }

    @Test
    public void testNavToAssociationClassProperty() {
        VirtualGroup vg1  = new VirtualGroup();
        vg1.setName("vg1");

        WorkspaceElement we1 = new WorkspaceElement();
        we1.setName("we1");
        WorkspaceElement we2 = new WorkspaceElement();
        we2.setName("we2");

        VirtualGroupWorkspaceElementAC ac1 = new VirtualGroupWorkspaceElementAC();
        ac1.setName("ac1");
        VirtualGroupWorkspaceElementAC ac2 = new VirtualGroupWorkspaceElementAC();
        ac2.setName("ac2");

        vg1.addToWorkspaceelement(we1,ac1);
        vg1.addToWorkspaceelement(we2, ac2);

        UMLG.get().commit();

        vg1.reload();
        Assert.assertEquals(2, vg1.getVirtualGroupWorkspaceElementAC().size());
        for (VirtualGroupWorkspaceElementAC virtualGroupWorkspaceElementAC : vg1.getVirtualGroupWorkspaceElementAC()) {
            System.out.println(virtualGroupWorkspaceElementAC.getName());
        }

        Assert.assertEquals(2, vg1.getWorkspaceelement().size());
        //This will null pointer if its the wrong type
        for (WorkspaceElement we: vg1.getWorkspaceelement()) {
            System.out.println(we.getName());
        }
    }

    @Test
    public void testNavToAssociationClassPropertyOtherWayAround() {
        VirtualGroup vg1  = new VirtualGroup();
        vg1.setName("vg1");

        WorkspaceElement we1 = new WorkspaceElement();
        we1.setName("we1");
        WorkspaceElement we2 = new WorkspaceElement();
        we2.setName("we2");

        VirtualGroupWorkspaceElementAC ac1 = new VirtualGroupWorkspaceElementAC();
        ac1.setName("ac1");
        VirtualGroupWorkspaceElementAC ac2 = new VirtualGroupWorkspaceElementAC();
        ac2.setName("ac2");

        we1.addToVirtualgroup(vg1, ac1);
        we2.addToVirtualgroup(vg1, ac2);

        UMLG.get().commit();

        vg1.reload();
        Assert.assertEquals(2, vg1.getVirtualGroupWorkspaceElementAC().size());
        for (VirtualGroupWorkspaceElementAC virtualGroupWorkspaceElementAC : vg1.getVirtualGroupWorkspaceElementAC()) {
            System.out.println(virtualGroupWorkspaceElementAC.getName());
        }

        Assert.assertEquals(2, vg1.getWorkspaceelement().size());
        //This will null pointer if its the wrong type
        for (WorkspaceElement we: vg1.getWorkspaceelement()) {
            System.out.println(we.getName());
        }
    }
}
