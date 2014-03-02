package org.umlg.gremlin.groovy

import com.tinkerpop.gremlin.groovy.Gremlin
import com.tinkerpop.pipes.Pipe

class UmlgGremlinGroovyGraphPropertyNames {

    public static void definePropertyNames() {
            Gremlin.addStep("demoQuickPreview_org_umlg_Company_name");
        Pipe.metaClass.demoQuickPreview_org_umlg_Company_name = {
            return _().property("demoQuickPreview::org::umlg::Company::name");
        }
    Gremlin.addStep("demoQuickPreview_org_umlg_Company_employee");
        Pipe.metaClass.demoQuickPreview_org_umlg_Company_employee = {
            return _().property("demoQuickPreview::org::umlg::Company::employee");
        }
    Gremlin.addStep("demoQuickPreview_org_umlg_Person_firstname");
        Pipe.metaClass.demoQuickPreview_org_umlg_Person_firstname = {
            return _().property("demoQuickPreview::org::umlg::Person::firstname");
        }
    Gremlin.addStep("demoQuickPreview_org_umlg_Person_lastname");
        Pipe.metaClass.demoQuickPreview_org_umlg_Person_lastname = {
            return _().property("demoQuickPreview::org::umlg::Person::lastname");
        }
    Gremlin.addStep("demoQuickPreview_org_umlg_worksFor_employer");
        Pipe.metaClass.demoQuickPreview_org_umlg_worksFor_employer = {
            return _().property("demoQuickPreview::org::umlg::worksFor::employer");
        }
    }

}
