package com.olxapplication.controller;


import java.util.List;
import java.util.Optional;

import com.olxapplication.dtos.UserDTO;
import com.olxapplication.dtos.UserDetailsDTO;
import com.olxapplication.service.UserService;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * This controller class provides API endpoints for managing users within the application.
 */
@Controller
@CrossOrigin
@RequestMapping(value = "/user")
@Setter
@Getter
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Get all users.
     * @return ModelAndView containing the list of users.
     */
    @GetMapping("/get")
    public ModelAndView getUsers(){
        List<UserDetailsDTO> users = userService.findUsers();
        ModelAndView mav = new ModelAndView("AdminGetUsers");
        mav.addObject("users", users);
        return mav;
    }

    /**
     * Insert a new user.
     * @param user The user to be inserted.
     * @param redirectAttributes Redirect attributes.
     * @return ModelAndView for redirection.
     */
    @PostMapping("/insert")
    public ModelAndView insertUser(@ModelAttribute("user") UserDetailsDTO user, RedirectAttributes redirectAttributes) {
        String msg = userService.insert(user);
        ModelAndView mav = new ModelAndView("redirect:/user/get");
        redirectAttributes.addFlashAttribute("message", msg);
        return mav;
    }

    /**
     * Get a user by its ID.
     * @param userId The ID of the user.
     * @return ResponseEntity containing the user details.
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<UserDetailsDTO> getUser(@PathVariable("id") String userId) {
        UserDetailsDTO userDto = userService.findUserById(userId);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    /**
     * Delete a user by its ID.
     * @param userId The ID of the user.
     * @param redirectAttributes Redirect attributes.
     * @return ModelAndView for redirection.
     */
    @PostMapping("/delete/{id}")
    public ModelAndView deleteUser(@PathVariable("id") String userId, RedirectAttributes redirectAttributes) {
        String msg = userService.deleteUserById(userId);
        redirectAttributes.addFlashAttribute("message", msg);
        ModelAndView mav = new ModelAndView("redirect:/user/get");
        return mav;
    }

    /**
     * Get users by name.
     * @param name The name of the user.
     * @return ResponseEntity containing the list of users.
     */
    @GetMapping("/filter/{name}")
    public ResponseEntity<List<UserDetailsDTO>> getUserByName(@PathVariable("name") String name) {
        List<UserDetailsDTO> dtos = userService.findUserFirstNameOrLastName(name, name);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /**
     * Update a user by its ID.
     * @param userId The ID of the user.
     * @param userDTO The user details.
     * @param redirectAttributes Redirect attributes.
     * @return ModelAndView for redirection.
     */
    @PostMapping("/update/{id}")
    public ModelAndView updateUser(@PathVariable("id") String userId, @ModelAttribute("user") UserDetailsDTO userDTO, RedirectAttributes redirectAttributes) {
        String msg = userService.updateUserById(userId, userDTO);
        redirectAttributes.addFlashAttribute("message", msg);
        ModelAndView mav = new ModelAndView("redirect:/user/get");
        return mav;
    }
}
