package org.umlg.runtime.adaptor;

import org.umlg.runtime.notification.ChangeHolder;
import org.umlg.runtime.notification.NotificationListener;

import java.util.*;


public class TransactionThreadNotificationVar {

    private TransactionThreadNotificationVar() {
    }

    private static ThreadLocal<Map<NotificationListener, List<ChangeHolder>>> transactionNotificationVar = new ThreadLocal<Map<NotificationListener, List<ChangeHolder>>>() {
        @Override
        protected Map<NotificationListener, List<ChangeHolder>> initialValue() {
            return new HashMap<>();
        }
    };

    public static void remove() {
        transactionNotificationVar.get().clear();
        transactionNotificationVar.remove();
    }

    public static Map<NotificationListener, List<ChangeHolder>> get() {
        return transactionNotificationVar.get();
    }

    public static void add(NotificationListener notificationListener, ChangeHolder changeHolder) {
        List<ChangeHolder> changeHolders = transactionNotificationVar.get().get(notificationListener);
        if (changeHolders == null) {
            changeHolders = new ArrayList<>();
            transactionNotificationVar.get().put(notificationListener, changeHolders);
        }
        changeHolders.add(changeHolder);
    }


}
