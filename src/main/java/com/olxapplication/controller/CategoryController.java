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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
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
     * Get all categories.
     * @return ModelAndView containing the list of categories.
     */
    @GetMapping("/get")
    public ModelAndView getCategorys(){
        List<CategoryDetailsDTO> dtos = categoryService.findCategories();
        ModelAndView mav = new ModelAndView("AdminGetCategories");
        mav.addObject("categories", dtos);
        return mav;
    }

    /**
     * Insert a new category.
     * @param categoryDTO The category to be inserted.
     * @param redirectAttributes Redirect attributes.
     * @return ModelAndView redirecting to "/category/get".
     */
    @PostMapping("/insert")
    public ModelAndView insertCategory(@ModelAttribute("category") CategoryDTO categoryDTO, RedirectAttributes redirectAttributes) {
        categoryDTO.setAnnounces(new ArrayList<>());
        String msg = categoryService.insert(categoryDTO);
        redirectAttributes.addFlashAttribute("message", msg);
        ModelAndView mav = new ModelAndView("redirect:/category/get");
        return mav;
    }

//    /**
//     * Get a category by its ID.
//     * @param categoryId The ID of the category.
//     * @return ResponseEntity containing the category details.
//     */
//    @GetMapping("/get/{id}")
//    public ResponseEntity<CategoryDetailsDTO> getCategory(@PathVariable("id") String categoryId) {
//        CategoryDetailsDTO categoryDto = categoryService.findCategoryById(categoryId);
//        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
//    }

//    /**
//     * Get categories by name.
//     * @param name The name of the category.
//     * @return ResponseEntity containing the list of categories.
//     */
//    @GetMapping("/byName/{name}")
//    public ResponseEntity<List<CategoryDetailsDTO>> getCategoryByName(@PathVariable("name") String name) {
//        List<CategoryDetailsDTO> categoryDto = categoryService.findCategoriesByName(name);
//        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
//    }

    /**
     * Delete a category by its ID.
     * @param categoryId The ID of the category.
     * @param redirectAttributes Redirect attributes( the response message to be displayed ).
     * @return ModelAndView redirecting to "/category/get".
     */
    @PostMapping("/delete/{id}")
    public ModelAndView deleteCategory(@PathVariable("id") String categoryId, RedirectAttributes redirectAttributes) {
        String msg = categoryService.deleteCategoryById(categoryId);
        redirectAttributes.addFlashAttribute("message", msg);
        ModelAndView mav = new ModelAndView("redirect:/category/get");
        return mav;
    }

    /**
     * Update a category name by its ID.
     * @param categoryId The ID of the category.
     * @param categoryDTO The category details.
     * @param redirectAttributes Redirect attributes( the response message to be displayed ).
     * @return ModelAndView redirecting to "/category/get".
     */
    @PostMapping("/update/{id}")
    public ModelAndView updateCategoryName(@PathVariable("id") String categoryId, @ModelAttribute("category") CategoryDetailsDTO categoryDTO, RedirectAttributes redirectAttributes) {
        String msg = categoryService.updateCategoryNameById(categoryId, categoryDTO);
        redirectAttributes.addFlashAttribute("message", msg);
        ModelAndView mav = new ModelAndView("redirect:/category/get");
        return mav;
    }
}
