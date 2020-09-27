package org.feichtmeier.genericwebapp.view;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.server.StreamResource;

import org.apache.commons.io.IOUtils;
import org.feichtmeier.genericwebapp.entity.Project;
import org.feichtmeier.genericwebapp.entity.ProjectImage;
import org.feichtmeier.genericwebapp.repository.GenericRepository;
import org.feichtmeier.genericwebapp.repository.ProjectImageRepository;

public class ProjectEditor extends GenericEntityEditor<Project> {

    private static final long serialVersionUID = 1L;

    private final TextField name;
    private Image projectImageComponent;
    private final HorizontalLayout imageContainer;
    private final Upload upload;
    private final HorizontalLayout uploadLayout;

    private ProjectImageRepository projectImageRepository;

    public ProjectEditor(GenericRepository<Project> repository, GenericGridView<Project> view) {
        super(repository, view);
        projectImageComponent = new Image("https://dummyimage.com/600x200/e7ebef/cccccf", "projectimage");
        projectImageComponent.setWidth("100%");
        projectImageComponent.setHeight(null);
        projectImageComponent.getStyle().set("border-radius", "5px");

        imageContainer = new HorizontalLayout(projectImageComponent);
        imageContainer.setWidthFull();
        imageContainer.setHeight(null);

        name = new TextField("Project name");
        name.setWidthFull();

        MemoryBuffer buffer = new MemoryBuffer();
        upload = new Upload(buffer);
        upload.addSucceededListener(event -> {
            imageContainer.removeAll();
            if (null != currentEntity.getProjectImage()) {
                projectImageRepository.delete(currentEntity.getProjectImage());
            }
            imageContainer.add(createImageComponent(event.getMIMEType(), event.getFileName(), buffer.getInputStream()));

            upload.getElement().executeJs("this.files=[]");

            ByteArrayOutputStream pngContent = new ByteArrayOutputStream();
            try {
                ImageIO.write(ImageIO.read(buffer.getInputStream()), "png", pngContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ProjectImage projectImage = new ProjectImage(pngContent.toByteArray(), event.getFileName(),
                    event.getMIMEType(), currentEntity);
            projectImageRepository.save(projectImage);
            currentEntity.setProjectImage(projectImage);

        });
        upload.getStyle().set("flex-grow", "1");
        upload.setWidthFull();
        uploadLayout = new HorizontalLayout(upload);
        uploadLayout.setMargin(false);
        uploadLayout.setWidthFull();

        binder.bind(name, "name");

        topLayout.add(name, uploadLayout, imageContainer);
    }

    @Override
    public Binder<Project> createBinder() {
        return new Binder<>(Project.class);
    }

    @Override
    protected void forgetSpecificSelections() {
    }

    @Override
    protected void editSpecificWidgets(Project entity) {
        imageContainer.removeAll();
        if (null != entity.getProjectImage()) {
            imageContainer.add(generateImage(entity.getProjectImage()));
        }
    }

    @Override
    protected String getDefaultEntityName(Project entity) {
        return entity.getName();
    }

    @Override
    protected void saveSpecificEntities() {

    }

    @Override
    protected void deleteSpecificEntities() {
    }

    private Image generateImage(ProjectImage projectImage) {
        StreamResource sr = new StreamResource("activityImage", () -> {
            return new ByteArrayInputStream(projectImage.getByteArray());
        });
        sr.setContentType("image/png");
        Image image = new Image(sr, projectImage.getFileName());
        image.setWidth("100%");
        image.setHeight(null);
        image.getStyle().set("border-radius", "5px");

        return image;
    }

    private Image createImageComponent(String mimeType, String fileName, InputStream stream) {
        Image image = new Image();
        try {

            byte[] bytes = IOUtils.toByteArray(stream);
            image.getElement().setAttribute("src", new StreamResource(fileName, () -> new ByteArrayInputStream(bytes)));
            try (ImageInputStream in = ImageIO.createImageInputStream(new ByteArrayInputStream(bytes))) {
                final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
                if (readers.hasNext()) {
                    ImageReader reader = readers.next();
                    try {
                        reader.setInput(in);
                        image.setWidth("100%");
                        image.setHeight(null);
                        image.getStyle().set("border-radius", "5px");
                    } finally {
                        reader.dispose();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    public void setProjectImageRepository(ProjectImageRepository projectImageRepository) {
        this.projectImageRepository = projectImageRepository;
    }
}
