package project.onlinestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.onlinestore.domain.entities.CartItemEntity;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {

    Optional<CartItemEntity> findByProductIdAndUserUsername(Long id, String username);

    List<CartItemEntity> findAllByUserUsername(String username);

    @Transactional
    void deleteAllByUserUsername(String username);
}
