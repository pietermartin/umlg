package org.umlg.java.metamodel.java8;

import org.umlg.java.metamodel.OJBlock;
import org.umlg.java.metamodel.OJPathName;
import org.umlg.java.metamodel.OJSimpleStatement;
import org.umlg.java.metamodel.OJStatement;
import org.umlg.java.metamodel.utilities.JavaStringHelpers;

import java.util.Set;

/**
 * Date: 2014/06/18
 * Time: 10:27 PM
 */
public class ForEachStatement extends OJStatement {

    private String collection = "";
    private String elemName = "";
    private OJBlock body = new OJBlock();

    public ForEachStatement(String collection, String elemName) {
        this.collection = collection;
        this.elemName = elemName;
    }

    public void addStatement(OJStatement ojStatement) {
        this.body.addToStatements(ojStatement);
    }

    public void addStatement(String ojStatement) {
        this.body.addToStatements(new OJSimpleStatement(ojStatement));
    }

    public String toJavaString(){
        String result = this.collection + ".forEach (\n";
        result = result + JavaStringHelpers.indent(this.elemName, 1) + " -> {\n";
        result = result + JavaStringHelpers.indent(this.body.toJavaString(), 2);
        result = result + JavaStringHelpers.indent("\n}", 1);
        result = result + JavaStringHelpers.indent("\n);", 0);
        return result;
    }

    @Override
    public OJStatement getDeepCopy() {
        return null;
    }

    @Override
    public void renameAll(Set<OJPathName> match, String suffix) {

    }
}
