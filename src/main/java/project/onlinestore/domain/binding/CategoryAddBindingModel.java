package project.onlinestore.domain.binding;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class CategoryAddBindingModel {

    private String name;
    private MultipartFile image;
    private Integer position;

    public CategoryAddBindingModel() {
    }

    @NotNull(message = "Полето е задължително!")
    @Size(min = 3, max = 50, message = "Името на категорията не може да е по-малка от 3 и по-голяма от 50 символа!")
    public String getName() {
        return name;
    }

    public CategoryAddBindingModel setName(String name) {
        this.name = name;
        return this;
    }

    public MultipartFile getImage() {
        return image;
    }

    public CategoryAddBindingModel setImage(MultipartFile image) {
        this.image = image;
        return this;
    }

    @NotNull(message = "Полето е задължително!")
    @Positive(message = "Позицията не може да бъде отрицателно число!")
    public Integer getPosition() {
        return position;
    }

    public CategoryAddBindingModel setPosition(Integer position) {
        this.position = position;
        return this;
    }
}
