package project.onlinestore.service;

import project.onlinestore.domain.service.RoleServiceModel;
import project.onlinestore.domain.service.UserServiceModel;

import java.util.Set;

public interface RoleService {

    void seedRolesInDb();

    void assignUserRoles(UserServiceModel userServiceModel, long numberOfUsers);

    Set<RoleServiceModel> findAllRoles();

    RoleServiceModel findByAuthority(String authority);
}
