package org.feichtmeier.genericwebapp.view;

import java.util.Map;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import org.feichtmeier.genericwebapp.entity.ProjectImage;

public class ImageLayout extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    private final Button removeImageButton;
    private boolean markedForDelete;

    public ImageLayout(Image image, boolean unpersisted, Map<ImageLayout, ProjectImage> imageLayoutToImageMap) {
        image.setMaxWidth("10em");
        image.setHeight("auto");
        image.getStyle().set("margin-bottom", "auto");
        image.getStyle().set("padding", "1em");
        image.getStyle().set("border-radius", "0.5em");
        String borderColor = unpersisted == true ? "var(--lumo-success-color)" : "var(--lumo-primary-color)";
        image.getStyle().set("border", "1px dashed " + borderColor);
        this.setAlignItems(Alignment.START);
        this.setJustifyContentMode(JustifyContentMode.START);
        this.getStyle().set("padding-left", "0");
        removeImageButton = new Button("", VaadinIcon.TRASH.create(), e -> {
            this.setEnabled(false);
            image.getStyle().set("border", "1px dashed var(--lumo-error-color)");
            markedForDelete = true;
        });
        removeImageButton.getElement().getThemeList().add("error");
        this.add(image, removeImageButton);
    }

    public Button getRemoveImageButton() {
        return removeImageButton;
    }

    public boolean isMarkedForDelete() {
        return markedForDelete;
    }

    public void setMarkedForDelete(boolean markedForDelete) {
        this.markedForDelete = markedForDelete;
    }
    
}
