package project.onlinestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.onlinestore.domain.entities.CategoryEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    @Query("SELECT c FROM CategoryEntity c order by c.position")
    List<CategoryEntity> getAllByPosition();

    Optional<CategoryEntity> findByName(String name);

    Optional<CategoryEntity> findByNameLatin(String nameLatin);

}
