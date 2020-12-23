package org.feichtmeier.genericwebapp.view;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

import org.apache.commons.io.IOUtils;

@CssImport("./styles/views/settings-view.css")
@VaadinSessionScope
@org.springframework.stereotype.Component
public class SettingsView extends AbstractView {

    private static final long serialVersionUID = 1L;

    private final Anchor logout = new Anchor("logout", "Logout");
    private final H3 header = new H3("Settings");
    private final Image defaultAvatarImage;
    private final HorizontalLayout avatarContainer;
    private final Upload upload;
    private final MemoryBuffer buffer;

    public SettingsView() {
        defaultAvatarImage = new Image("https://dummyimage.com/50x50/e7ebef/3b3b3b", "Avatar");
        avatarContainer = new HorizontalLayout(defaultAvatarImage);

        buffer = new MemoryBuffer();
        upload = new Upload(buffer);
        upload.setUploadButton(new Button("Avatar..."));
        upload.setDropLabel(new Label("Drop image here"));
        upload.setAcceptedFileTypes("image/png", "image/jpeg");
        upload.addSucceededListener(event -> {
            if (event.getMIMEType().startsWith("image")) {
                Component component = createImage(event.getMIMEType(), event.getFileName(),
                        buffer.getInputStream());
                avatarContainer.removeAll();
                avatarContainer.add(component);
            }
        });

        add(header, avatarContainer, upload, new HorizontalLayout(logout));
    }

    @Override
    public void linkComponentsToCss() {
        setId("settings-view");
        defaultAvatarImage.setClassName("avatar");
        avatarContainer.setId("avatar-container");
        logout.setId("logout");

    }

    private Image createImage(String mimeType, String fileName, InputStream stream) {
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
                        image.setClassName("avatar");
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
}
