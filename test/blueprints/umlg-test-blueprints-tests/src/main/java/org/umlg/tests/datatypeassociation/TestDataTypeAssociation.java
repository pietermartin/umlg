package org.umlg.tests.datatypeassociation;

import org.junit.Assert;
import org.junit.Test;
import org.umlg.datatypeassociation.SOFTWARE_VERSION;
import org.umlg.datatypeassociation.VENDOR;
import org.umlg.datatypeassociation.VENDOR_TECH;
import org.umlg.datatypeassociation.VENDOR_TECH_PARSER;

/**
 * Date: 2014/10/09
 * Time: 5:43 PM
 */
public class TestDataTypeAssociation {

    @Test
    public void testEnumerationAssocciation() {
        Assert.assertEquals(VENDOR_TECH.HUAWEI_GSM, SOFTWARE_VERSION.HUAWEI_GSM_R15.getVendorTech());
        Assert.assertEquals(VENDOR_TECH.HUAWEI_UMTS, SOFTWARE_VERSION.HUAWEI_UMTS_R15.getVendorTech());
        Assert.assertTrue(VENDOR_TECH.HUAWEI_GSM.getSoftwareVersion().contains(SOFTWARE_VERSION.HUAWEI_GSM_R15));
        Assert.assertFalse(VENDOR_TECH.HUAWEI_GSM.getSoftwareVersion().contains(SOFTWARE_VERSION.HUAWEI_UMTS_R15));

        Assert.assertEquals(VENDOR.ERICSSON, SOFTWARE_VERSION.ERICSSON_GSM_R13.getVendorTech().getVENDOR());
        Assert.assertEquals(VENDOR.ERICSSON, SOFTWARE_VERSION.ERICSSON_UMTS_W12.getVendorTech().getVENDOR());
        Assert.assertEquals(VENDOR.HUAWEI, SOFTWARE_VERSION.HUAWEI_GSM_R15.getVendorTech().getVENDOR());
        Assert.assertEquals(VENDOR.HUAWEI, SOFTWARE_VERSION.HUAWEI_UMTS_R15.getVendorTech().getVENDOR());

        Assert.assertTrue(VENDOR_TECH.HUAWEI_GSM.getVendorTechParser().contains(VENDOR_TECH_PARSER.HUAWEI_GSM_CSV));
    }

}
