package project.onlinestore.web.controllers.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import project.onlinestore.domain.binding.SupplierAddBindingModel;
import project.onlinestore.domain.entities.SupplierEntity;
import project.onlinestore.domain.service.SupplierServiceModel;
import project.onlinestore.repository.SupplierRepository;
import project.onlinestore.repository.UserRepository;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@WithMockUser(username = "test", roles = {"ADMIN", "ROOT"})
public class SupplierControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        SupplierEntity supplierEntity = initSupplier();
        supplierRepository.save(supplierEntity);
    }

    @After
    public void tearDown() {
        supplierRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ROOT", "ROLE_ADMIN"})
    public void get_SupplierPage_ReturnsOk() throws Exception {
        mockMvc.perform(get("/admin/suppliers"))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("allSuppliers"))
                .andExpect(model().attributeExists("allSuppliers"))
                .andExpect(view().name("/admin/supplier/all-suppliers"));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ROOT", "ROLE_ADMIN"})
    public void get_SupplierAddPage_ReturnsOk() throws Exception {
        mockMvc.perform(get("/admin/suppliers/add"))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("addSuppliers"))
                .andExpect(view().name("/admin/supplier/add-supplier"));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ROOT", "ROLE_ADMIN"})
    public void addSupplier_InvalidInput() throws Exception {
        SupplierServiceModel supplierServiceModel = modelMapper.map(initSupplier(), SupplierServiceModel.class);

        mockMvc.perform(post("/admin/suppliers/add")
                        .content(objectMapper.writeValueAsString(supplierServiceModel))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().methodName("addSuppliersConfirm"))
                .andExpect(flash().attributeExists("supplierAddBindingModel"))
                .andExpect(redirectedUrl("/suppliers/add"));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ROOT", "ROLE_ADMIN"})
    public void addNewSupplier() throws Exception {
        SupplierAddBindingModel supplierAddBindingModel = new SupplierAddBindingModel();
        supplierAddBindingModel.setName("testov");
        supplierAddBindingModel.setEmail("testov@testov.bg");
        supplierAddBindingModel.setPhoneNumber("0889559966");
        supplierAddBindingModel.setPerson("testov");
        supplierAddBindingModel.setAddress("testov 69");

        mockMvc.perform(post("/admin/suppliers/add")
                        .sessionAttr("supplier", supplierAddBindingModel)
                        .param("name", supplierAddBindingModel.getName())
                        .param("email", supplierAddBindingModel.getEmail())
                        .param("phoneNumber", supplierAddBindingModel.getPhoneNumber())
                        .param("person", supplierAddBindingModel.getPerson())
                        .param("address", supplierAddBindingModel.getAddress())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().methodName("addSuppliersConfirm"))
                .andExpect(redirectedUrl("/admin/suppliers"));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ROOT", "ROLE_ADMIN"})
    public void get_EditSupplierPage_ReturnsOk() throws Exception {
        SupplierEntity test = supplierRepository.findByName("test").get();

        mockMvc.perform(get("/admin/suppliers/edit/" + test.getId()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("editSupplier"))
                .andExpect(model().attributeExists("supplier"))
                .andExpect(view().name("/admin/supplier/edit-supplier"));
    }

    @Test
    public void get_CategoryEditCategory_InvalidInput() throws Exception {
        SupplierEntity supplierEntity = supplierRepository.findByName("test").get();

        SupplierAddBindingModel supplierAddBindingModel = new SupplierAddBindingModel();
        supplierAddBindingModel.setName("q");
        supplierAddBindingModel.setEmail("q.bg");
        supplierAddBindingModel.setAddress("q");
        supplierAddBindingModel.setPerson("q");
        supplierAddBindingModel.setPhoneNumber("0000000000000000000");

        mockMvc.perform(post("/admin/suppliers/edit/" + supplierEntity.getId())
                        .sessionAttr("supplier", supplierEntity)
                        .param("name", supplierAddBindingModel.getName())
                        .param("email", supplierAddBindingModel.getEmail())
                        .param("phoneNumber", supplierAddBindingModel.getPhoneNumber())
                        .param("person", supplierAddBindingModel.getPerson())
                        .param("address", supplierAddBindingModel.getAddress())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().methodName("editSupplierConfirm"))
                .andExpect(flash().attributeExists("supplierAddBindingModel"))
                .andExpect(redirectedUrl("/admin/suppliers/edit/" + supplierEntity.getId()));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ROOT", "ROLE_ADMIN"})
    public void get_EditSupplierConfirm() throws Exception {
        SupplierEntity test = supplierRepository.findByName("test").get();

        mockMvc.perform(post("/admin/suppliers/edit/" + test.getId())
                        .sessionAttr("supplier", test)
                        .sessionAttr("supplier", test)
                        .param("name", test.getName())
                        .param("email", test.getEmail())
                        .param("phoneNumber", test.getPhoneNumber())
                        .param("person", test.getPerson())
                        .param("address", test.getAddress())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().methodName("editSupplierConfirm"))
                .andExpect(redirectedUrl("/admin/suppliers"));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ROOT", "ROLE_ADMIN"})
    public void get_DeleteSupplierPage() throws Exception {
        SupplierEntity test = supplierRepository.findByName("test").get();

        mockMvc.perform(get("/admin/suppliers/delete/" + test.getId()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("deleteSupplier"))
                .andExpect(model().attributeExists("supplier"))
                .andExpect(view().name("/admin/supplier/delete-supplier"));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ROOT", "ROLE_ADMIN"})
    public void get_DeleteSupplierConfirm() throws Exception {
        SupplierEntity test = supplierRepository.findByName("test").get();
        mockMvc.perform(post("/admin/suppliers/delete/" + test.getId()))
                .andExpect(handler().methodName("deleteSupplierConfirm"))
                .andExpect(redirectedUrl("/admin/suppliers"));

    }

    private SupplierEntity initSupplier() {
        SupplierEntity supplierEntity = new SupplierEntity();
        return supplierEntity.setName("test")
                .setEmail("test@test.bg")
                .setPhoneNumber("0889559966")
                .setPerson("Testpv")
                .setAddress("Testova 69");
    }
}