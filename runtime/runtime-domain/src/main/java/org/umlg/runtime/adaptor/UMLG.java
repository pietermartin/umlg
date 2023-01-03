package org.umlg.runtime.adaptor;


public class UMLG {

    private UMLG() {
    }

    private static final ThreadLocal<UmlgGraph> dbVar = ThreadLocal.withInitial(() -> UmlgGraphManager.INSTANCE.startupGraph());

    public static UmlgGraph get() {
        return dbVar.get();
    }

    public static void remove() {
        dbVar.remove();
    }

}
