package com.b2.e_commerce.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.b2.e_commerce.dto.CategoryRequestDTO;
import com.b2.e_commerce.dto.CategoryResponseDTO;
import com.b2.e_commerce.entity.Category;
import com.b2.e_commerce.exception.ResourceNotFoundException;
import com.b2.e_commerce.repository.CategoryRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final com.b2.e_commerce.repository.ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository,
                           com.b2.e_commerce.repository.ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public List<CategoryResponseDTO> findAll() {
        return categoryRepository.findByActiveTrue()
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    public CategoryResponseDTO findById(Long id) {
    	Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category avec id " + id + " introuvable"));

        return mapToResponse(category);
    }

    public CategoryResponseDTO create(CategoryRequestDTO dto) {
    	Category category = new Category();
    	category.setName(dto.getName());
    	category.setDescription(dto.getDescription());
    	category.setType(dto.getType());

    	category.setActive(true);

    	Category saved = categoryRepository.save(category);
        return mapToResponse(saved);
    }

    public CategoryResponseDTO update(Long id, CategoryRequestDTO dto) {
    	Category category = categoryRepository.findById(id)
        	.orElseThrow(() -> new ResourceNotFoundException("Category avec id " + id + " introuvable")  );

    	category.setName(dto.getName());
    	category.setDescription(dto.getDescription());
    	category.setType(dto.getType());

    	category.setActive(true);

    	Category saved = categoryRepository.save(category);
        return mapToResponse(saved);
    }

    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category avec id " + id + " introuvable") );

        if (!productRepository.findByCategoryAndActiveTrue(category).isEmpty()) {
            category.setActive(false);
            categoryRepository.save(category);
            return;
        }

        categoryRepository.delete(category);
    }

    private CategoryResponseDTO mapToResponse(Category category) {
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setType(category.getType());
        dto.setActive(Boolean.TRUE.equals(category.isActive()));
        return dto;
    }
}
