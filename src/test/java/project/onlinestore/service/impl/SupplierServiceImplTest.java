package project.onlinestore.service.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import project.onlinestore.domain.entities.SupplierEntity;
import project.onlinestore.domain.service.SupplierServiceModel;
import project.onlinestore.domain.view.SupplierViewModel;
import project.onlinestore.repository.SupplierRepository;
import project.onlinestore.service.SupplierService;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class SupplierServiceImplTest {

    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private ModelMapper modelMapper;

    SupplierEntity supplier;
    SupplierEntity supplier2;

    @Before
    public void setup() {
        supplierRepository.deleteAll();

        supplier = new SupplierEntity();
        supplier.setName("TestSupplier-1")
                .setEmail("TestSupplier@TestSupplier.bg")
                .setPhoneNumber("123456789")
                .setAddress("Somewhere")
                .setPerson("Gosho")
                .setId(1L);

        supplier2 = new SupplierEntity();
        supplier2.setName("TestSupplier-2")
                .setEmail("TestSupplier-2@TestSupplier.bg")
                .setPhoneNumber("987654321")
                .setAddress("Somewhere")
                .setPerson("Gosho")
                .setId(2L);

    }

    @Test
    public void test_SeedSuppliers() {
        supplierService.seedSuppliers();
        Assert.assertEquals(5, supplierRepository.count());
    }

    @Test
    public void test_GetAllSuppliers() {
        supplierRepository.saveAll(List.of(supplier, supplier2));
        Assert.assertEquals(2, supplierRepository.count());

        List<SupplierViewModel> allSuppliers = supplierService.getAllSuppliers();
        Assert.assertEquals(supplierRepository.count(), allSuppliers.size());
    }

    @Test
    public void test_FindSupplierByName() {
        supplierRepository.saveAll(List.of(supplier, supplier2));
        Assert.assertEquals(2, supplierRepository.count());

        SupplierEntity supplierByName = supplierService.findSupplierByName(supplier.getName());
        Assert.assertNotNull(supplierByName);

        Assert.assertEquals(supplierByName.getName(), supplier.getName());
    }

    @Test
    public void test_AddSupplier() {
        Assert.assertEquals(0, supplierRepository.count());
        supplierService.addSupplier(modelMapper.map(supplier, SupplierServiceModel.class));
        Assert.assertEquals(1, supplierRepository.count());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_AddSupplier_Throws() {
        supplierRepository.save(supplier);
        Assert.assertEquals(1, supplierRepository.count());

        supplierService.addSupplier(modelMapper.map(supplier, SupplierServiceModel.class));
    }

    @Test
    public void test_FindSupplierById() {
        supplierRepository.save(supplier);
        Assert.assertEquals(1, supplierRepository.count());

        SupplierEntity supplierById = supplierService.findSupplierById(supplier.getId());
        Assert.assertEquals(supplier.getId(), supplierById.getId());
    }

    @Test
    public void test_EditSupplier() {
        supplierRepository.save(supplier);
        Assert.assertEquals(1, supplierRepository.count());
        SupplierEntity supplierByName = supplierService.findSupplierByName(supplier.getName());
        Assert.assertNotNull(supplierByName);
        supplierByName.setName("Gosho")
                .setEmail("gosho@gosh.bg");

        supplierService.editSupplier(supplierByName.getId(), modelMapper.map(supplierByName, SupplierServiceModel.class));

        Assert.assertEquals("Gosho", supplierByName.getName());
    }

    @Test
    public void test_DeleteSupplier() {
        supplierRepository.save(supplier);
        Assert.assertEquals(1, supplierRepository.count());

        SupplierEntity supplierByName = supplierService.findSupplierByName(supplier.getName());
        Assert.assertNotNull(supplierByName);

        supplierService.deleteSupplier(supplierByName.getId());
        Assert.assertEquals(0, supplierRepository.count());
    }
}