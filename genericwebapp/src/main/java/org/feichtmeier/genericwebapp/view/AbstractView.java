package org.feichtmeier.genericwebapp.view;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public abstract class AbstractView extends VerticalLayout implements Styleable {

    private static final long serialVersionUID = -3471469020050609980L;

    public void createNotification(String text) {
        Notification notification = new Notification(text);
        notification.setDuration(2000);
        notification.open();
    }
    
}