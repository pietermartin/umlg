package org.umlg.associationclass.gremlin;

import org.junit.Test;
import org.umlg.runtime.adaptor.UMLG;
import org.umlg.runtime.adaptor.UmlgQueryEnum;
import org.umlg.runtime.test.BaseLocalDbTest;
import org.umlg.tinkergraph.Human;

/**
 * Date: 2014/03/19
 * Time: 10:28 PM
 */
public class TestUmlgProperties extends BaseLocalDbTest {

    @Test
    public void testProperties() {
        Human human = new Human();
        human.setName("john");
        db.commit();

        String result = UMLG.get().executeQueryToString(UmlgQueryEnum.GROOVY, human.getId(), "self.has('associationclass::org::umlg::tinkergraph::Human::name', T.eq, 'john')");
        System.out.println(result);
    }
}
