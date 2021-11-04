package project.onlinestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.onlinestore.domain.entities.SupplierEntity;

import java.util.Optional;


@Repository
public interface SupplierRepository extends JpaRepository<SupplierEntity, Long> {

 Optional<SupplierEntity> findByName(String name);
}
