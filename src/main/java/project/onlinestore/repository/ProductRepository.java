package project.onlinestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.onlinestore.domain.entities.ProductEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByProductName(String productName);

    Optional<ProductEntity> findByProductCode(String productCode);

    @Query("SELECT p FROM ProductEntity p WHERE p.category.name = :name")
    List<ProductEntity> findAllByCategory(String name);

    Optional <ProductEntity> findByProductNameLatin(String name);

}
