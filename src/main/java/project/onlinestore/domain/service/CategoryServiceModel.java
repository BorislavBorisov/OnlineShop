package project.onlinestore.domain.service;

public class CategoryServiceModel extends BaseServiceModel {

    private String name;
    private String nameLatin;
    private String imgUrl;
    private Integer position;

    public CategoryServiceModel() {
    }

    public String getName() {
        return name;
    }

    public CategoryServiceModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getNameLatin() {
        return nameLatin;
    }

    public CategoryServiceModel setNameLatin(String nameLatin) {
        this.nameLatin = nameLatin;
        return this;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public CategoryServiceModel setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    public Integer getPosition() {
        return position;
    }

    public CategoryServiceModel setPosition(Integer position) {
        this.position = position;
        return this;
    }

}
