package org.feichtmeier.genericwebapp.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import org.feichtmeier.genericwebapp.entity.AbstractEntity;
import org.feichtmeier.genericwebapp.repository.GenericRepository;

public abstract class GenericGridView<E extends AbstractEntity> extends VerticalLayout {

    private static final long serialVersionUID = -151705160904157799L;

    protected final Grid<E> grid;
    protected final GenericEntityEditor<E> entityEditor;
    protected final Button newEntityButton;
    protected E currentEntity;

    protected GenericRepository<E> repository;

    public abstract Grid<E> createGrid();

    public abstract GenericEntityEditor<E> createEditor();

    public abstract E createEmptyEntity();

    protected abstract String[] createWantedColumnNames();

    public GenericGridView(GenericRepository<E> repository) {

        this.repository = repository;
        grid = createGrid();
        grid.removeAllColumns();
        grid.addColumns(createWantedColumnNames());
        grid.setHeightByRows(true);
        currentEntity = createEmptyEntity();
        entityEditor = createEditor();
        newEntityButton = new Button(VaadinIcon.PLUS.create(), e -> {
            entityEditor.editEntity(createEmptyEntity());            
        });
        grid.asSingleSelect().addValueChangeListener(event -> {
            entityEditor.editEntity(event.getValue());
            grid.setItems(repository.findAll());
        });
        entityEditor.setVisible(false);
        add(grid, newEntityButton, entityEditor);
        grid.setItems(repository.findAll());
    }
}