package org.umlg.runtime.adaptor;


public class TransactionThreadBypassValidationVar {

    private TransactionThreadBypassValidationVar() {
    }

    private static ThreadLocal<Boolean> transactionBypassValidationVar = ThreadLocal.withInitial(() -> false);

    public static void remove() {
        transactionBypassValidationVar.remove();
    }

    public static void set(boolean bypass) {
        transactionBypassValidationVar.set(bypass);
    }

    public static boolean get() {
        return transactionBypassValidationVar.get();
    }

}
