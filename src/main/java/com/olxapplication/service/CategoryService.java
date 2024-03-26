package com.olxapplication.service;

import com.olxapplication.dtos.CategoryDTO;
import com.olxapplication.dtos.CategoryDetailsDTO;
import com.olxapplication.exception.ResourceNotFoundException;
import com.olxapplication.mappers.CategoryMapper;
import com.olxapplication.entity.Category;
import com.olxapplication.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This service layer class provides business logic operations for managing categories within the application.
 * It interacts with the CategoryRepository to perform CRUD operations and leverages mappers for DTO conversions.
 */
@Service
@AllArgsConstructor
public class CategoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(com.olxapplication.service.CategoryService.class);
    private final CategoryRepository categoryRepository;

    /**
     * Retrieves a list of all category details available in the system.
     *
     * @return A list of CategoryDetailsDTO objects representing all categories.
     */
    public List<CategoryDetailsDTO> findCategories() {
        List<Category> categoryList = categoryRepository.findAll();
        return categoryList.stream()
                .map(CategoryMapper::toCategoryDetailsDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of all category details that have matching name available in the system.
     *
     * @return A list of CategoryDetailsDTO objects representing all categories with a matching name.
     */
    public List<CategoryDetailsDTO> findCategoriesByName(String name) {
        List<Category> categoryList = categoryRepository.findCategoriesByCategoryNameContainsIgnoreCase(name);
        return categoryList.stream()
                .map(CategoryMapper::toCategoryDetailsDTO)
                .collect(Collectors.toList());
    }

    public CategoryDetailsDTO findCategoryByName(String name) {
        List<Category> categoryList = categoryRepository.findCategoriesByCategoryNameContainsIgnoreCase(name);
        return CategoryMapper.toCategoryDetailsDTO(categoryList.getFirst());
    }

    /**
     * Retrieves the details of a specific category identified by its unique String.
     *
     * @param id The unique identifier of the category to retrieve.
     * @return A CategoryDetailsDTO object representing the requested category, or throws an exception if not found.
     * @throws ResourceNotFoundException If no category with the provided ID exists.
     */
    public CategoryDetailsDTO findCategoryById(String id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (!categoryOptional.isPresent()) {
            LOGGER.debug("Category with id {" + id + "} was not found in db");
            throw new ResourceNotFoundException(Category.class.getSimpleName() + "with id: " + id);
        }
        return CategoryMapper.toCategoryDetailsDTO(categoryOptional.get());
    }

    /**
     * Creates a new category in the system based on the provided details.
     *
     * @param categoryDTO A CategoryDTO object containing the data for the new category.
     * @return The String of the newly created category.
     */
    public String insert(CategoryDTO categoryDTO) {
        Category category = CategoryMapper.toEntity(categoryDTO);
        category = categoryRepository.save(category);
        LOGGER.debug("Category with id {} was inserted in db", category.getId());
        return category.getId();
    }

    /**
     * Deletes a category identified by its unique String.
     *
     * @param id The unique identifier of the category to be deleted.
     * @return The String of the deleted category, or throws an exception if not found.
     * @throws ResourceNotFoundException If no category with the provided ID exists.
     */
    public String deleteCategoryById(String id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (!categoryOptional.isPresent()) {
            LOGGER.error("Category with id {} was not found in db", id);
        } else {
            categoryRepository.delete(categoryOptional.get());
            LOGGER.debug("Category with id {} was successfully deleted", id);

        }
        return categoryOptional.get().getId();
    }

    /**
     * Updates the name of an existing category identified by its unique String.
     *
     * @param id The unique identifier of the category to be updated.
     * @param categoryDTO A CategoryDetailsDTO object containing the updated category name.
     * @return A CategoryDetailsDTO object representing the updated category, or throws an exception if not found.
     * @throws ResourceNotFoundException If no category with the provided ID exists.
     */
    public CategoryDetailsDTO updateCategoryNameById(String id, CategoryDetailsDTO categoryDTO) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);

        if (!categoryOptional.isPresent()) {
            LOGGER.error("Category with id {} was not found in db", id);
        } else {
            Category toBeUpdated = categoryOptional.get();
            toBeUpdated.setCategoryName(categoryDTO.getCategoryName());
//            toBeUpdated.setAnnounces(categoryDTO.getAnnounces());
            categoryRepository.save(toBeUpdated);
            LOGGER.debug("Category with id {} was successfully updated", id);
        }
        return CategoryMapper.toCategoryDetailsDTO(categoryOptional.get());
    }

}
