package project.onlinestore.domain.binding;

import org.springframework.web.multipart.MultipartFile;

public class ImageBindingModel {

    private MultipartFile image;

    public ImageBindingModel() {
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
