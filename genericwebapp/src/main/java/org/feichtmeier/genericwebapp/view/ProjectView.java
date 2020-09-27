package org.feichtmeier.genericwebapp.view;

import java.util.List;

import com.vaadin.flow.component.grid.Grid;

import org.feichtmeier.genericwebapp.entity.Project;
import org.feichtmeier.genericwebapp.repository.GenericRepository;
import org.feichtmeier.genericwebapp.repository.ProjectImageRepository;
import org.springframework.security.access.annotation.Secured;

@Secured(ViewNames.PROJECT_VIEW)
public class ProjectView extends GenericGridView<Project> {

    private static final long serialVersionUID = 1L;

    private ProjectEditor projectEditor;

    public ProjectView(GenericRepository<Project> repository, ProjectImageRepository projectImageRepository) {
        super(repository);
        grid.setItems(getAllowedEntities());
        projectEditor.setProjectImageRepository(projectImageRepository);
    }

    @Override
    public Grid<Project> createGrid() {
        Grid<Project> grid = new Grid<>(Project.class);
        grid.removeAllColumns();
        grid.addColumn("name");
        return grid;
    }

    @Override
    public List<Project> getAllowedEntities() {
        return repository.findAll();
    }

    @Override
    public GenericEntityEditor<Project> createEditor() {
        ProjectEditor projectEditor = new ProjectEditor(this.repository, this);
        this.projectEditor = projectEditor;
        return projectEditor;
    }

    @Override
    public Project createEmptyEntity() {
        return new Project("");
    }

    @Override
    protected List<Project> mainFilterOperation(String filterText) {
        return repository.findAll();
    }
    
}
