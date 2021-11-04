package project.onlinestore.domain.binding;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class CategoryAddBindingModel {

    private String name;
    private String imgUrl;
    private Integer position;

    public CategoryAddBindingModel() {
    }

    @NotNull(message = "Полето е задължително!")
    @Size(min = 3, max = 50, message = "Името на категорията не може да е по-малка от 3 и по-голяма от 50 символа!")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @NotNull(message = "Полето е задължително!")
    @Positive(message = "Позицията не може да бъде отрицателно число!")
    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}
