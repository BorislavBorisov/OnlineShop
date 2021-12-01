package project.onlinestore.web.controllers.admin;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.onlinestore.domain.binding.SupplierAddBindingModel;
import project.onlinestore.domain.service.SupplierServiceModel;
import project.onlinestore.domain.view.SupplierViewModel;
import project.onlinestore.service.SupplierService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class SupplierController {

    private final SupplierService supplierService;
    private final ModelMapper modelMapper;

    public SupplierController(SupplierService supplierService, ModelMapper modelMapper) {
        this.supplierService = supplierService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/suppliers")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String allSuppliers(Model model) {
        model.addAttribute("allSuppliers", this.supplierService.getAllSuppliers());
        return "/admin/supplier/all-suppliers";
    }

    @GetMapping("/suppliers/add")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String addSuppliers() {
        return "/admin/supplier/add-supplier";
    }

    @PostMapping("/suppliers/add")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String addSuppliersConfirm(@Valid SupplierAddBindingModel supplierAddBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("supplierAddBindingModel", supplierAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.supplierAddBindingModel", bindingResult);

            return "redirect:/suppliers/add";
        }

        this.supplierService
                .addSupplier(this.modelMapper.map(supplierAddBindingModel, SupplierServiceModel.class));
        return "redirect:/admin/suppliers";
    }

    @GetMapping("/suppliers/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String editSupplier(@PathVariable Long id, Model model) {
        model.addAttribute("supplier", this.modelMapper.map(this.supplierService.findSupplierById(id), SupplierViewModel.class));
        return "/admin/supplier/edit-supplier";
    }

    @PostMapping("/suppliers/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String editSupplierConfirm(@PathVariable Long id, @Valid SupplierAddBindingModel supplierAddBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("supplierAddBindingModel", supplierAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.supplierAddBindingModel", bindingResult);

            return "redirect:/admin/suppliers/edit/" + id;
        }
        SupplierServiceModel map = this.modelMapper.map(supplierAddBindingModel, SupplierServiceModel.class);
        this.supplierService.editSupplier(id, map);
        return "redirect:/admin/suppliers";
    }

    @GetMapping("suppliers/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String deleteSupplier(@PathVariable Long id, Model model) {
        model.addAttribute("supplier", this.modelMapper.map(this.supplierService.findSupplierById(id), SupplierViewModel.class));
        return "/admin/supplier/delete-supplier";
    }

    @PostMapping("suppliers/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String deleteSupplierConfirm(@PathVariable Long id) {
        this.supplierService.deleteCategory(id);
        return "redirect:/admin/suppliers";
    }

    @GetMapping("/suppliers/fetch")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public List<SupplierViewModel> fetchCategories() {
        return this.supplierService.getAllSuppliers();
    }

    @ModelAttribute
    public SupplierAddBindingModel supplierAddBindingModel() {
        return new SupplierAddBindingModel();
    }
}
