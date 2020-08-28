package org.feichtmeier.genericwebapp.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.Binder;

import org.feichtmeier.genericwebapp.entity.AbstractEntity;
import org.feichtmeier.genericwebapp.repository.GenericRepository;

public abstract class GenericEntityEditor<E extends AbstractEntity> extends FormLayout {

    private static final long serialVersionUID = -7882218816479297372L;

    protected E currentEntity;

    private final HorizontalLayout buttonLayout;

    protected final Binder<E> binder;

    protected final Button saveButton, cancelButton, deleteButton;

    public abstract Binder<E> createBinder();

    GenericRepository<E> repository;

    private Grid<E> grid;

    protected Button newEntityButton;

    protected abstract void createSpecialWidgets(E entity);

    public GenericEntityEditor(Grid<E> grid, GenericRepository<E> repository) {

        this.repository = repository;

        this.grid = grid;

        binder = createBinder();

        saveButton = new Button(VaadinIcon.CHECK.create(), e -> {
            repository.save(currentEntity);
            this.grid.setItems(repository.findAll());
            this.setVisible(false);
            this.grid.setVisible(true);
            this.newEntityButton.setVisible(true);
        });
        cancelButton = new Button(VaadinIcon.CLOSE.create(), e-> {
            this.setVisible(false);
            this.grid.setVisible(true);
            this.newEntityButton.setVisible(true);
        });
        deleteButton = new Button(VaadinIcon.TRASH.create(), e-> {
            repository.delete(currentEntity);
            this.grid.setItems(repository.findAll());
            this.setVisible(false);
            this.grid.setVisible(true);
            this.newEntityButton.setVisible(true);
        });
        saveButton.getElement().getThemeList().add("primary");
        deleteButton.getElement().getThemeList().add("error");
        buttonLayout = new HorizontalLayout(saveButton, cancelButton, deleteButton);
        add(buttonLayout, 2);
    }

    public void editEntity(E entity) {
        if (entity == null) {
            return;
        }
        
        currentEntity = entity;
        binder.setBean(currentEntity);
        createSpecialWidgets(currentEntity);   
        
        this.setVisible(true);
        this.grid.setVisible(false);
        this.newEntityButton.setVisible(false);
    }

	public void setNewEntityButton(Button newEntityButton) {
        this.newEntityButton = newEntityButton;
    }

}