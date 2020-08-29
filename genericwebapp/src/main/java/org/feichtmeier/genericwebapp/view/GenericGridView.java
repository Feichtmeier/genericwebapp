package org.feichtmeier.genericwebapp.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import org.feichtmeier.genericwebapp.entity.AbstractEntity;
import org.feichtmeier.genericwebapp.repository.GenericRepository;

public abstract class GenericGridView<E extends AbstractEntity> extends AbstractView {

    private static final long serialVersionUID = -151705160904157799L;

    protected final Grid<E> grid;
    protected final GenericEntityEditor<E> entityEditor;
    private final Button newEntityButton;
    protected final TextField entityFilter;
    protected final HorizontalLayout topLayout;
    protected final VerticalLayout scrollableLayout;
    protected E currentEntity;

    protected GenericRepository<E> repository;

    public abstract Grid<E> createGrid();

    public abstract GenericEntityEditor<E> createEditor();

    public abstract E createEmptyEntity();

    public GenericGridView(GenericRepository<E> repository) {
        super();
        this.setSizeFull();

        topLayout = new HorizontalLayout();
        entityFilter = new TextField("", "Search ...");
        
        this.repository = repository;
        grid = createGrid();
        grid.setHeightByRows(true);
        currentEntity = createEmptyEntity();
        entityEditor = createEditor();
        newEntityButton = new Button(VaadinIcon.PLUS.create(), e -> {
            entityEditor.editEntity(createEmptyEntity());
        });

        topLayout.add(newEntityButton, entityFilter);
        topLayout.setWidthFull();
        
        entityEditor.setTopLayout(topLayout);

        grid.asSingleSelect().addValueChangeListener(event -> {
            entityEditor.editEntity(event.getValue());
            grid.setItems(repository.findAll());
        });
        entityEditor.setVisible(false);
        
        grid.setItems(repository.findAll());

        scrollableLayout = new VerticalLayout();
        scrollableLayout.add(grid);
        scrollableLayout.setWidthFull();
        scrollableLayout.setHeight(null);
        scrollableLayout.getStyle().set("overflow-y", "auto");
        entityEditor.setScrollableLayout(scrollableLayout);

        add(topLayout, entityEditor, scrollableLayout);
    }
}