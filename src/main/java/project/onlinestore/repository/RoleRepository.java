package project.onlinestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.onlinestore.domain.entities.RoleEntity;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    RoleEntity findByAuthority(String authority);
}
