package project.onlinestore.service;

import project.onlinestore.domain.entities.SupplierEntity;
import project.onlinestore.domain.service.SupplierServiceModel;
import project.onlinestore.domain.view.SupplierViewModel;

import java.util.List;

public interface SupplierService {

    List<SupplierViewModel> getAllSuppliers();

    SupplierServiceModel addSupplier(SupplierServiceModel supplierServiceModel);

    SupplierEntity findSupplierById(Long id);

    SupplierServiceModel editSupplier(Long id, SupplierServiceModel supplierServiceModel);

    boolean deleteSupplier(Long id);

    void seedSuppliers();

    SupplierEntity findSupplierByName(String name);
}
