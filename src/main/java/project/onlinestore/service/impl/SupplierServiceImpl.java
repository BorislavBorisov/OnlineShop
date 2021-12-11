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
import java.util.Optional;
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
    public SupplierEntity findSupplierById(Long id) {
        return this.supplierRepository
                .findById(id).orElse(null);
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
    public boolean deleteSupplier(Long id) {
        SupplierEntity supplier =
                this.supplierRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid supplier ID!"));

            this.supplierRepository.delete(supplier);
            return true;
    }

    @Override
    public void seedSuppliers() {
        if (this.supplierRepository.count() == 0) {
            SupplierEntity supplierEntity = new SupplierEntity();
            supplierEntity.setName("Фончо ЕООД")
                    .setEmail("foncho@foncho.bg")
                    .setAddress("гр. София, бул. Черни връх 180")
                    .setPerson("Фончо Тариката")
                    .setPhoneNumber("0869696969")
                    .setRegistered(Instant.now());

            SupplierEntity supplierEntity1 = new SupplierEntity();
            supplierEntity1.setName("Най-добрата фирма ЕООД")
                    .setEmail("toni@storaro.bg")
                    .setAddress("1700 Студентски Комплекс, София")
                    .setPerson("Тони Стораро")
                    .setPhoneNumber("0866666666")
                    .setRegistered(Instant.now());

            SupplierEntity supplierEntity2 = new SupplierEntity();
            supplierEntity2.setName("Най-сериозната фирма ООД")
                    .setEmail("gosho@gosho.bg")
                    .setAddress("ул. „св. Георги Софийски“ 3, 1606 Център, София")
                    .setPerson("Георги Сериозното")
                    .setPhoneNumber("0829629629")
                    .setRegistered(Instant.now());

            SupplierEntity supplierEntity3 = new SupplierEntity();
            supplierEntity3.setName("Victoria LTD")
                    .setEmail("viki@gmail.com")
                    .setAddress("гр. Елин Пелин, бул. Витоша 8")
                    .setPerson("Виктория")
                    .setPhoneNumber("0812345678")
                    .setRegistered(Instant.now());

            SupplierEntity supplierEntity4 = new SupplierEntity();
            supplierEntity4.setName("True or false")
                    .setEmail("true@false.com")
                    .setAddress("Няма адрес")
                    .setPerson("Anonymous")
                    .setPhoneNumber("+45********")
                    .setRegistered(Instant.now());

            this.supplierRepository.saveAll(
                    List.of(supplierEntity, supplierEntity1, supplierEntity2, supplierEntity3, supplierEntity4)
            );
        }
    }

    @Override
    public SupplierEntity findSupplierByName(String name) {
        return this.supplierRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Invalid input"));
    }

    @Override
    public boolean supplierNameCheck(String name) {
        Optional<SupplierEntity> byName = this.supplierRepository.findByName(name);
        return byName.isPresent();
    }
}
