package project.onlinestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.onlinestore.domain.entities.CartEntity;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {
}
