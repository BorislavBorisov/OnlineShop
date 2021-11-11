package project.onlinestore.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.onlinestore.domain.entities.SupplierEntity;
import project.onlinestore.domain.service.SupplierServiceModel;
import project.onlinestore.domain.view.SupplierViewModel;
import project.onlinestore.repository.SupplierRepository;
import project.onlinestore.service.SupplierService;

import java.time.Instant;
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

    @Override
    public SupplierServiceModel findSupplierById(Long id) {
        SupplierEntity supplier = this.supplierRepository
                .findById(id).orElse(null);

        return this.modelMapper.map(supplier, SupplierServiceModel.class);
    }

    @Override
    public SupplierServiceModel editSupplier(Long id, SupplierServiceModel supplierServiceModel) {
        SupplierEntity supplier = this.supplierRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid supplier ID!"));

        supplier.setName(supplierServiceModel.getName())
                .setPerson(supplierServiceModel.getPerson())
                .setPhoneNumber(supplierServiceModel.getPhoneNumber())
                .setEmail(supplierServiceModel.getEmail())
                .setAddress(supplierServiceModel.getAddress())
                .setModified(Instant.now());

        return this.modelMapper.map(this.supplierRepository.save(supplier), SupplierServiceModel.class);
    }

    @Override
    public boolean deleteCategory(Long id) {
        SupplierEntity supplier =
                this.supplierRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid supplier ID!"));

        try {
            this.supplierRepository.delete(supplier);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
}
