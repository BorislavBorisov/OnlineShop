package project.onlinestore.domain.service;

public abstract class BaseServiceModel {

    private Long id;

    protected BaseServiceModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
