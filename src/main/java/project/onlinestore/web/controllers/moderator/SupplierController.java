package project.onlinestore.web.controllers.moderator;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.onlinestore.domain.binding.CategoryAddBindingModel;
import project.onlinestore.domain.binding.SupplierAddBindingModel;
import project.onlinestore.domain.service.CategoryServiceModel;
import project.onlinestore.domain.service.SupplierServiceModel;
import project.onlinestore.domain.view.CategoryViewModel;
import project.onlinestore.domain.view.SupplierViewModel;
import project.onlinestore.service.SupplierService;

import javax.validation.Valid;

@Controller
@RequestMapping("/moderator")
public class SupplierController {

    private final SupplierService supplierService;
    private final ModelMapper modelMapper;

    public SupplierController(SupplierService supplierService, ModelMapper modelMapper) {
        this.supplierService = supplierService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/suppliers")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public String allSuppliers(Model model) {
        model.addAttribute("allSuppliers", this.supplierService.getAllSuppliers());
        return "/moderator/supplier/all-suppliers";
    }

    @GetMapping("/suppliers/add")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public String addSuppliers() {
        return "/moderator/supplier/add-supplier";
    }

    @PostMapping("/suppliers/add")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public String addSuppliersConfirm(@Valid SupplierAddBindingModel supplierAddBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("supplierAddBindingModel", supplierAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.supplierAddBindingModel", bindingResult);

            return "redirect:add";
        }

        this.supplierService.addSupplier(this.modelMapper.map(supplierAddBindingModel, SupplierServiceModel.class));
        return "redirect:/moderator/suppliers";
    }

    @GetMapping("/suppliers/edit/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public String editSupplier(@PathVariable Long id, Model model) {
        model.addAttribute("supplier", this.modelMapper.map(this.supplierService.findSupplierById(id), SupplierViewModel.class));
        return "/moderator/supplier/edit-supplier";
    }

    @PostMapping("/suppliers/edit/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public String editSupplierConfirm(@PathVariable Long id, @Valid SupplierAddBindingModel supplierAddBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("supplierAddBindingModel", supplierAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.supplierAddBindingModel", bindingResult);

            return "redirect:" + id;
        }
        SupplierServiceModel map = this.modelMapper.map(supplierAddBindingModel, SupplierServiceModel.class);
        this.supplierService.editSupplier(id, map);
        return "redirect:/moderator/suppliers";
    }

    @GetMapping("suppliers/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public String deleteSupplier(@PathVariable Long id, Model model) {
        model.addAttribute("supplier", this.modelMapper.map(this.supplierService.findSupplierById(id), SupplierViewModel.class));
        return "/moderator/supplier/delete-supplier";
    }

    @PostMapping("suppliers/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public String deleteSupplierConfirm(@PathVariable Long id) {
        this.supplierService.deleteCategory(id);
        return "redirect:/moderator/suppliers";
    }

    @ModelAttribute
    public SupplierAddBindingModel supplierAddBindingModel() {
        return new SupplierAddBindingModel();
    }
}
