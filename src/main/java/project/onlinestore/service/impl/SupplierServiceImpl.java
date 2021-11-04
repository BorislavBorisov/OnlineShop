package project.onlinestore.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.onlinestore.domain.entities.SupplierEntity;
import project.onlinestore.domain.service.SupplierServiceModel;
import project.onlinestore.domain.view.SupplierViewModel;
import project.onlinestore.repository.SupplierRepository;
import project.onlinestore.service.SupplierService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;

    public SupplierServiceImpl(SupplierRepository supplierRepository, ModelMapper modelMapper) {
        this.supplierRepository = supplierRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<SupplierViewModel> getAllSuppliers() {
        return this.supplierRepository
                .findAll()
                .stream()
                .map(s -> this.modelMapper.map(s, SupplierViewModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public SupplierServiceModel addSupplier(SupplierServiceModel supplierServiceModel) {
        SupplierEntity supplierEntity = this.supplierRepository.findByName(supplierServiceModel.getName())
                .orElse(null);

        if (supplierEntity == null) {
            SupplierEntity supplier = this.modelMapper.map(supplierServiceModel, SupplierEntity.class);
            return this.modelMapper.map(
                    this.supplierRepository.save(supplier), SupplierServiceModel.class);
        }

        throw new IllegalArgumentException("Supplier with that name already exists!");
    }
}
