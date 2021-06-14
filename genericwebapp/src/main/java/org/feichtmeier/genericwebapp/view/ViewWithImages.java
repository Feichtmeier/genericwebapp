package org.feichtmeier.genericwebapp.view;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.server.StreamResource;

import org.apache.commons.io.IOUtils;
import org.feichtmeier.genericwebapp.entity.AbstractImage;

public class ViewWithImages extends AbstractView {

    protected final MemoryBuffer buffer;
    protected final MultiFileMemoryBuffer multiBuffer;


    public ViewWithImages(MemoryBuffer buffer, MultiFileMemoryBuffer multiBuffer) {
        this.buffer = buffer;
        this.multiBuffer = multiBuffer;
    }

    @Override
    public void linkComponentsToCss() {
        
    }
    
    public Image createImageFromUpload(String mimeType, String fileName, InputStream stream) {
        final Image image = new Image();
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

    public Image createImageFromEntity(AbstractImage imageEntity) {
        final StreamResource sr = new StreamResource("avatarImage", () -> {
            return new ByteArrayInputStream(imageEntity.getByteArray());
        });
        sr.setContentType("image/png");
        final Image image = new Image(sr, imageEntity.getFileName());
        image.setClassName("avatar");

        return image;
    }
}
