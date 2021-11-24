package project.onlinestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.onlinestore.domain.entities.CartEntity;
import project.onlinestore.domain.entities.UserEntity;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {

    CartEntity findCartEntityByUser(UserEntity user);

    CartEntity findCartByUserUsername(String name);
}
