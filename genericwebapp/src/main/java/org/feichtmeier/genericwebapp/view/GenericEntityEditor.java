package org.feichtmeier.genericwebapp.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;

import org.feichtmeier.genericwebapp.entity.AbstractEntity;
import org.feichtmeier.genericwebapp.repository.GenericRepository;

public abstract class GenericEntityEditor<E extends AbstractEntity> extends Dialog {

    private static final long serialVersionUID = -7882218816479297372L;

    protected E currentEntity;
    protected final VerticalLayout topLayout;
    protected final HorizontalLayout bottomLayout;
    protected final VerticalLayout dialogBody;
    protected final Binder<E> binder;
    protected final Button saveButton, cancelButton, deleteButton;
    protected final GenericRepository<E> repository;
    protected final GenericGridView<E> view;

    public GenericEntityEditor(GenericRepository<E> repository, GenericGridView<E> view) {

        this.repository = repository;
        this.view = view;
        binder = createBinder();

        saveButton = new Button("", VaadinIcon.CHECK.create(), e -> {
            if (binder.validate().isOk()) {
                repository.save(currentEntity);
                saveSpecificEntities();            
                createNotification("Saved", currentEntity);
                view.refresh();
                goBackToView();
            } else {
                createNotification("NOT saved", currentEntity);
            }
        });
        saveButton.getElement().getThemeList().add("primary");
        saveButton.getStyle().set("flex-grow", "1");
        saveButton.getStyle().set("margin-top", "0");
        saveButton.getStyle().set("margin-bottom", "0");

        cancelButton = new Button("", VaadinIcon.CLOSE.create(), e -> {
            goBackToView();
        });
        cancelButton.getStyle().set("flex-grow", "1");
        cancelButton.getStyle().set("margin-top", "0");
        cancelButton.getStyle().set("margin-bottom", "0");

        deleteButton = new Button("", VaadinIcon.TRASH.create(), e -> {
            deleteSpecificEntities();
            repository.delete(currentEntity);
            createNotification("Deleted", currentEntity);
            view.refresh();
            goBackToView();
        });
        deleteButton.getElement().getThemeList().add("error");
        deleteButton.getStyle().set("flex-grow", "1");
        deleteButton.getStyle().set("margin-top", "0");
        deleteButton.getStyle().set("margin-bottom", "0");

        bottomLayout = new HorizontalLayout(saveButton, cancelButton, deleteButton);
        bottomLayout.setAlignItems(Alignment.CENTER);
        bottomLayout.setWidthFull();
        bottomLayout.getStyle().set("flex-grow", "0");

        topLayout = new VerticalLayout();
        topLayout.setPadding(false);
        topLayout.getStyle().set("flex-grow", "1");
        topLayout.setWidthFull();
        topLayout.setHeight(null);
        topLayout.getStyle().set("overflow-y", "auto");
        topLayout.setPadding(false);
        topLayout.setMargin(false);

        dialogBody = new VerticalLayout(topLayout, bottomLayout);
        dialogBody.setHeightFull();
        dialogBody.setPadding(false);
        dialogBody.setMargin(false);
        
        add(dialogBody);
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        setHeight(null);
    }


    public void editEntity(E entity) {
        this.open();
        if (entity == null) {
            return;
        }

        this.currentEntity = entity;
        binder.setBean(currentEntity);
        editSpecificWidgets(currentEntity);

        this.setVisible(true);
    }

    private void createNotification(String prefix, E entity) {
        Notification notification = new Notification(prefix + " " + getDefaultEntityName(entity));
        notification.setDuration(2000);
        notification.open();
    }

    private void goBackToView() {
        forgetSpecificSelections();
        close();
    }

    public abstract Binder<E> createBinder();

    protected abstract void forgetSpecificSelections();

    protected abstract void editSpecificWidgets(E entity);

    protected abstract String getDefaultEntityName(E entity);
    
    protected abstract void saveSpecificEntities();

    protected abstract void deleteSpecificEntities();

}