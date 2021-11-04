package project.onlinestore.service;

import project.onlinestore.domain.service.SupplierServiceModel;
import project.onlinestore.domain.view.SupplierViewModel;

import java.util.List;

public interface SupplierService {

    List<SupplierViewModel> getAllSuppliers();

    SupplierServiceModel addSupplier(SupplierServiceModel supplierServiceModel);
}
