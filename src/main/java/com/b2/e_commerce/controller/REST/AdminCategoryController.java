package com.b2.e_commerce.controller.REST;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.b2.e_commerce.dto.CategoryRequestDTO;
import com.b2.e_commerce.dto.CategoryResponseDTO;
import com.b2.e_commerce.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/category")
public class AdminCategoryController {

    private final CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // GET ALL
    @GetMapping
    public List<CategoryResponseDTO> getAll() {
        return categoryService.findAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public CategoryResponseDTO getById(@PathVariable Long id) {
        return categoryService.findById(id);
    }

    // CREATE
    @PostMapping
    public CategoryResponseDTO create(@Valid @RequestBody CategoryRequestDTO dto) {
        return categoryService.create(dto);
    }

    // MODIFY
    @PutMapping("/{id}")
    public CategoryResponseDTO modify(@PathVariable Long id, @Valid @RequestBody CategoryRequestDTO dto) {
        return categoryService.update(id, dto);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
    	categoryService.delete(id);
    }
}
