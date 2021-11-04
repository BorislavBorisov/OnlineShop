package project.onlinestore.domain.entities;

import javax.persistence.*;
import java.time.Instant;

@MappedSuperclass
public abstract class BaseEntity {

    private Long id;
    private Instant registered;
    private Instant modified;

    public BaseEntity() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "registered")
    public Instant getRegistered() {
        return registered;
    }

    public void setRegistered(Instant registered) {
        this.registered = registered;
    }

    @Column(name = "modified")
    public Instant getModified() {
        return modified;
    }

    public void setModified(Instant modified) {
        this.modified = modified;
    }

    @PrePersist
    public void beforeRegistered() {
        setRegistered(Instant.now());
    }
}
