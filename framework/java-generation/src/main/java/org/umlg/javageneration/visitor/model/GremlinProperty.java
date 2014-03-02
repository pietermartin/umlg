package org.umlg.javageneration.visitor.model;

/**
 * Date: 2014/03/01
 * Time: 7:05 PM
 */
public class GremlinProperty {

    private String qualifiedName;

    public GremlinProperty(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public String getGremlinName() {
//        //tinkergraph____org____umlg____tinkergraph____Person____name
//        String[] split = this.qualifiedName.split("::");
//        StringBuilder sb = new StringBuilder();
//        int count = 1;
//        for (String s : split) {
//            sb.append(s);
//            if (count++ < split.length) {
//                sb.append("____");
//            }
//        }
//        return sb.toString();

        return this.qualifiedName.replace("::", "_").replace("<", "_").replace(">", "_");
    }

}
