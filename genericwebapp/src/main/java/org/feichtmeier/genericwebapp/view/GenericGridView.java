package org.feichtmeier.genericwebapp.view;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import org.apache.commons.lang3.StringUtils;
import org.feichtmeier.genericwebapp.entity.AbstractEntity;
import org.feichtmeier.genericwebapp.repository.GenericRepository;
import org.feichtmeier.genericwebapp.view.util.Resizeable;

public abstract class GenericGridView<E extends AbstractEntity> extends AbstractView implements Resizeable {

    private static final long serialVersionUID = -151705160904157799L;

    protected final Grid<E> grid;
    protected final GenericEntityEditor<E> entityEditor;
    protected final Button newEntityButton;
    protected final TextField entityFilter;
    protected final HorizontalLayout topLayout;
    protected final VerticalLayout scrollableLayout;
    protected GenericRepository<E> repository;

    public GenericGridView(GenericRepository<E> repository) {
        super();
        setSizeFull();

        topLayout = new HorizontalLayout();
        entityFilter = new TextField("", "Search ...");
        entityFilter.setValueChangeMode(ValueChangeMode.EAGER);
        entityFilter.addValueChangeListener(e -> listEntities(e.getValue()));
        entityFilter.setMinWidth("7em");
        entityFilter.getStyle().set("flex-grow", "1");

        this.repository = repository;
        grid = createGrid();
        grid.setHeightByRows(true);

        entityEditor = createEditor();
        newEntityButton = new Button(VaadinIcon.PLUS.create(), e -> {
            entityEditor.editEntity(createEmptyEntity());
        });

        topLayout.add(newEntityButton, entityFilter);
        topLayout.setWidthFull();        

        grid.asSingleSelect().addValueChangeListener(event -> {
            entityEditor.editEntity(event.getValue());
            refresh();
        });
        entityEditor.setVisible(false);

        scrollableLayout = new VerticalLayout();
        scrollableLayout.add(grid);
        scrollableLayout.setWidthFull();
        scrollableLayout.setHeight(null);
        scrollableLayout.getStyle().set("overflow-y", "auto");
        scrollableLayout.getStyle().set("padding", "0");
        grid.getStyle().set("overflow-y", "inherit");

        topLayout.getStyle().set("display", "flex");
        topLayout.getStyle().set("flex-direction", "row");
        newEntityButton.getStyle().set("flex-grow", "0");

        add(topLayout, scrollableLayout);
        setAlignItems(Alignment.CENTER);

        applyResponsivePadding(this, 20, 4);
    }

    private void listEntities(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            grid.setItems(getAllowedEntities());
        } else {
            grid.setItems(mainFilterOperation(filterText));
        }
    }

    @Override
    protected void refresh() {
        grid.setItems(getAllowedEntities());
    }

    public abstract Grid<E> createGrid();
    public abstract List<E> getAllowedEntities();
    public abstract GenericEntityEditor<E> createEditor();
    public abstract E createEmptyEntity();
    protected abstract List<E> mainFilterOperation(String filterText);

}