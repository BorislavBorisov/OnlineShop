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

    public BaseEntity setId(Long id) {
        this.id = id;
        return this;
    }

    @Column(name = "registered")
    public Instant getRegistered() {
        return registered;
    }

    public BaseEntity setRegistered(Instant registered) {
        this.registered = registered;
        return this;
    }

    @Column(name = "modified")
    public Instant getModified() {
        return modified;
    }

    public BaseEntity setModified(Instant modified) {
        this.modified = modified;
        return this;
    }

    @PrePersist
    public void beforeRegistered() {
        setRegistered(Instant.now());
    }
}
