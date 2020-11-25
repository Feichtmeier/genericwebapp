package org.feichtmeier.genericwebapp.view.util;

import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.FlexComponent;

public interface Resizeable {

    double levelOne = 1000;
    double levelTwo = levelOne * 1.92;

    public default void applyResponsivePadding(FlexComponent<?> layout, int desktopPadding, int mobilePadding) {
        UI.getCurrent().getPage().retrieveExtendedClientDetails(details -> {

            if(details.getWindowInnerWidth() <= levelOne) {
                layout.getStyle().set("padding-left", mobilePadding + "%");
                layout.getStyle().set("padding-right", mobilePadding + "%");
            }
            else if (details.getWindowInnerWidth() > levelOne && details.getWindowInnerWidth() <= levelTwo) {
                layout.getStyle().set("padding-left", desktopPadding + "%");
                layout.getStyle().set("padding-right", desktopPadding + "%");
            }
            else if(details.getWindowInnerWidth() > levelTwo) {
                layout.getStyle().set("padding-left", (desktopPadding *  1.5) + "%");
                layout.getStyle().set("padding-right", (desktopPadding *  1.5) + "%");
            }
            
            layout.setSizeFull();
        });

        UI.getCurrent().getPage().addBrowserWindowResizeListener(e -> {
            if (e.getWidth() <= levelOne) {
                layout.getStyle().set("padding-left", mobilePadding + "%");
                layout.getStyle().set("padding-right", mobilePadding + "%");
            } else if (e.getWidth() > levelOne && e.getWidth() <= levelTwo) {
                layout.getStyle().set("padding-left", desktopPadding + "%");
                layout.getStyle().set("padding-right", desktopPadding + "%");
            } else if(e.getWidth() > levelTwo) {
                layout.getStyle().set("padding-left", (desktopPadding * 1.5) + "%");
                layout.getStyle().set("padding-right", (desktopPadding * 1.5) + "%");
            }
            layout.setSizeFull();
        });
    }

    public default void addResponsiveDialogWidth(HasSize component) {
        UI.getCurrent().getPage().retrieveExtendedClientDetails(details -> {
            if (details.getWindowInnerWidth() <= levelOne) {
                component.setWidthFull();
            } else if(details.getWindowInnerWidth() > levelOne && details.getWindowInnerWidth() <= levelTwo) {
                component.setWidth("600px");
            } else if(details.getWindowInnerWidth() > levelTwo) {
                component.setWidth("600px");
            }

        });

        UI.getCurrent().getPage().addBrowserWindowResizeListener(e -> {
            if (e.getWidth() <= levelOne) {
                component.setWidthFull();
            } else if(e.getWidth() > levelOne && e.getWidth() <= levelTwo) {
                component.setWidth("600px");
            } else if(e.getWidth() > levelTwo) {
                component.setWidth("600px");
            }
        });
    }
}
