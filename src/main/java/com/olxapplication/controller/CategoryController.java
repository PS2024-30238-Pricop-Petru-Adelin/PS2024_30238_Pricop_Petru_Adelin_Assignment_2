package com.olxapplication.controller;

import com.olxapplication.dtos.CategoryDTO;
import com.olxapplication.dtos.CategoryDetailsDTO;
import com.olxapplication.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This controller class provides API endpoints for managing categories within the application.
 * */
@RestController
@CrossOrigin
@RequestMapping(value = "/category")
@Setter
@Getter
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * Retrieves a list of all available categories.
     *
     * @return A response entity containing a list of CategoryDetailsDTO objects representing all categories.
     */
    @GetMapping()
    public ResponseEntity<List<CategoryDetailsDTO>> getCategorys(){
        List<CategoryDetailsDTO> dtos = categoryService.findCategories();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /**
     * Creates a new category.
     *
     * @param categoryDTO The category details to be used for creation.
     *                        The request body should be a valid CategoryDTO object.
     * @return A response entity with the created category's ID upon successful creation,
     *         including a CREATED status code.
     */
    @PostMapping()
    public ResponseEntity<String> insertCategory(@RequestBody CategoryDTO categoryDTO) {
        String categoryID = categoryService.insert(categoryDTO);
        return new ResponseEntity<>(categoryID, HttpStatus.CREATED);
    }

    /**
     * Retrieves a specific category by its unique identifier.
     *
     * @param categoryId The unique identifier of the category to retrieve.
     * @return A response entity containing the CategoryDetailsDTO object for the retrieved category
     *         upon successful retrieval, including an OK status code.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDetailsDTO> getCategory(@PathVariable("id") String categoryId) {
        CategoryDetailsDTO categoryDto = categoryService.findCategoryById(categoryId);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    /**
     * Retrieves a specific category by its name.
     *
     * @param name The substring of the name of the categories to be retrieved.
     * @return A response entity containing the CategoryDetailsDTO object list for the retrieved categories
     *         upon successful retrieval, including an OK status code.
     */
    @GetMapping("/byName/{name}")
    public ResponseEntity<List<CategoryDetailsDTO>> getCategoryByName(@PathVariable("name") String name) {
        List<CategoryDetailsDTO> categoryDto = categoryService.findCategoriesByName(name);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    /**
     * Deletes a category identified by its unique identifier.
     *
     * @param categoryId The unique identifier of the category to be deleted.
     * @return An empty response entity upon successful deletion, including a NO_CONTENT status code.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") String categoryId) {
        String String = categoryService.deleteCategoryById(categoryId);
        return new ResponseEntity<>(String, HttpStatus.OK);
    }

    /**
     * Updates an existing category name with the provided details(the new name).
     *
     * @param categoryId The unique identifier of the category to be updated.
     * @param categoryDTO The category details containing the updated information.
     * @return A response entity containing the updated CategoryDetailsDTO object upon successful update,
     *         including an OK status code.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDetailsDTO> updateCategoryName(@PathVariable("id") String categoryId, @RequestBody CategoryDetailsDTO categoryDTO) {
        CategoryDetailsDTO category = categoryService.updateCategoryNameById(categoryId, categoryDTO);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }
}
