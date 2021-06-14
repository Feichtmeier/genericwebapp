package org.feichtmeier.genericwebapp.view;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

import javax.imageio.ImageIO;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

import org.feichtmeier.genericwebapp.entity.AvatarImage;
import org.feichtmeier.genericwebapp.security.SecurityUtils;
import org.feichtmeier.genericwebapp.service.AvatarImageService;
import org.feichtmeier.genericwebapp.service.UserService;

@CssImport("./styles/views/settings-view.css")
@VaadinSessionScope
@org.springframework.stereotype.Component
public class SettingsView extends ViewWithImages {

    private static final long serialVersionUID = 1L;

    private final Anchor logout = new Anchor("logout", "Logout");
    private final H3 header = new H3("Settings");
    private final Image defaultAvatarImage;
    private final HorizontalLayout imageContainer;
    private final Upload upload;
    private final String sessionUsername;

    public SettingsView(UserService userService, AvatarImageService avatarImageService) {
        super(new MemoryBuffer(), new MultiFileMemoryBuffer());

        sessionUsername = SecurityUtils.getUsername();

        AvatarImage userAvatar = avatarImageService.findMostRecentUserAvatarImage(sessionUsername);
        if (null == userAvatar) {
            defaultAvatarImage = new Image("https://dummyimage.com/50x50/e7ebef/3b3b3b", "Avatar");
        } else {
            defaultAvatarImage = createImageFromEntity(userAvatar);
        }

        imageContainer = new HorizontalLayout(defaultAvatarImage);
        upload = new Upload(buffer);
        upload.setUploadButton(new Button("Add image..."));
        upload.setDropLabel(new Label("Drop image here"));
        upload.setAcceptedFileTypes("image/png", "image/jpeg");
        upload.addSucceededListener(event -> {
            if (event.getMIMEType().startsWith("image")) {
                final Image image = createImageFromUpload(event.getMIMEType(), event.getFileName(),
                        buffer.getInputStream());
                imageContainer.removeAll();
                imageContainer.add(image);

                ByteArrayOutputStream pngContent = new ByteArrayOutputStream();
                try {
                    ImageIO.write(ImageIO.read(buffer.getInputStream()), "png", pngContent);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                final AvatarImage avatarImage = new AvatarImage(pngContent.toByteArray(), event.getFileName(),
                        event.getMIMEType(), userService.findByUsername(sessionUsername), LocalDateTime.now());
                avatarImageService.save(avatarImage);
            }
        });

        add(header, imageContainer, upload, new HorizontalLayout(logout));
    }

    @Override
    public void linkComponentsToCss() {
        setId("settings-view");
        defaultAvatarImage.setClassName("avatar");
        imageContainer.setId("avatar-container");
        logout.setId("logout");

    }
}
