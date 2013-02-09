package org.tuml.java.metamodel.annotation;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.tuml.java.metamodel.OJField;
import org.tuml.java.metamodel.OJPathName;
import org.tuml.java.metamodel.utilities.JavaStringHelpers;
import org.tuml.java.metamodel.utilities.JavaUtil;

public class OJAnnotatedField extends OJField implements OJAnnotatedElement {
    Map<OJPathName, OJAnnotationValue> f_annotations = new TreeMap<OJPathName, OJAnnotationValue>();
    private boolean isTransient = false;

    public OJAnnotatedField(String string, String ojPathName) {
        this.setName(string);
        this.setType(new OJPathName(ojPathName));
    }

    public OJAnnotatedField(String string, OJPathName ojPathName) {
        this.setName(string);
        this.setType(ojPathName);
    }

    public void setTransient(boolean a) {
        this.isTransient = a;
    }

    public Collection<OJAnnotationValue> getAnnotations() {
        return f_annotations.values();
    }

    public boolean addAnnotationIfNew(OJAnnotationValue value) {
        if (f_annotations.containsKey(value.getType())) {
            return false;
        } else {
            putAnnotation(value);
            return true;
        }
    }

    public OJAnnotationValue putAnnotation(OJAnnotationValue value) {
        return f_annotations.put(value.getType(), value);
    }

    public OJAnnotationValue removeAnnotation(OJPathName type) {
        return f_annotations.remove(type);
    }

    @Override
    public String toJavaString() {
        StringBuilder sb = new StringBuilder();
        if (!getComment().equals("")) {
            sb.append("\t// ");
            sb.append(getComment());
            sb.append("\n");
        }
        if (getAnnotations().size() > 0) {
            sb.append(JavaStringHelpers.indent(JavaUtil.collectionToJavaString(getAnnotations(), "\n"), 0));
            sb.append("\n");
        }
        if (this.getOwner() != null) { // field is part of block statement
            if (isTransient) {
                sb.append("transient ");
            }
            sb.append(visToJava(this));
        }
        if (sb.length() > 0) {
            sb.append(' ');
        }
        sb.append(getType().getCollectionTypeName());
        sb.append(' ');
        sb.append(getName());
        if (getInitExp() != null && !getInitExp().equals("")) {
            sb.append(" = ");
            sb.append(getInitExp());
        }
        sb.append(';');
        return sb.toString();
    }

    public OJAnnotatedField getDeepCopy() {
        OJAnnotatedField copy = new OJAnnotatedField(getName(), getType());
        copyDeepInfoInto(copy);
        return copy;
    }

    public void copyDeepInfoInto(OJAnnotatedField copy) {
        super.copyDeepInfoInto(copy);
        Collection<OJAnnotationValue> annotations = getAnnotations();
        for (OJAnnotationValue ojAnnotationValue : annotations) {
            OJAnnotationValue copyAnnotation = ojAnnotationValue.getDeepCopy();
            copy.addAnnotationIfNew(copyAnnotation);
        }
    }

    public void renameAll(Set<OJPathName> renamePathNames, String newName) {
        super.renameAll(renamePathNames, newName);
        Collection<OJAnnotationValue> annotations = getAnnotations();
        for (OJAnnotationValue ojAnnotationValue : annotations) {
            ojAnnotationValue.renameAll(renamePathNames, newName);
        }
    }

    public OJAnnotationValue findAnnotation(OJPathName path) {
        return AnnotationHelper.getAnnotation(this, path);
    }

    public void suppressUncheckedWarning() {
        addAnnotationIfNew(new OJAnnotationValue(new OJPathName("java.lang.SuppressWarnings"), "unchecked"));
    }
}
