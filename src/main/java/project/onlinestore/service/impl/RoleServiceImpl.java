package project.onlinestore.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.onlinestore.domain.entities.RoleEntity;
import project.onlinestore.domain.service.RoleServiceModel;
import project.onlinestore.repository.RoleRepository;
import project.onlinestore.service.RoleService;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedRolesInDb() {
        if (this.roleRepository.count() == 0) {
            this.roleRepository.saveAndFlush(new RoleEntity("ROLE_GUEST"));
            this.roleRepository.saveAndFlush(new RoleEntity("ROLE_CLIENT"));
            this.roleRepository.saveAndFlush(new RoleEntity("ROLE_SUPPLIER"));
            this.roleRepository.saveAndFlush(new RoleEntity("ROLE_MODERATOR"));
            this.roleRepository.saveAndFlush(new RoleEntity("ROLE_ADMIN"));
        }
    }

    @Override
    public Set<RoleServiceModel> findAllRoles() {
        return this.roleRepository
                .findAll()
                .stream()
                .map(r -> this.modelMapper.map(r, RoleServiceModel.class))
                .collect(Collectors.toSet());
    }

    @Override
    public RoleServiceModel findByAuthority(String authority) {
        return this.modelMapper.map(this.roleRepository.findByAuthority(authority), RoleServiceModel.class);
    }
}
