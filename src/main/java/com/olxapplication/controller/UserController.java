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
     * Retrieves a list of all registered users.
     *
     * @return A response entity containing a list of UserDetailsDTO objects representing all users.
     */
    @GetMapping("/get")
    public ModelAndView getUsers(){
        List<UserDetailsDTO> users = userService.findUsers();
        ModelAndView mav = new ModelAndView("AdminGetUsers");
        mav.addObject("users", users);
        //mav.setViewName("/get");
        return mav;
    }

//    @GetMapping("/insert")
//    public ModelAndView showInsertForm() {
//        ModelAndView modelAndView = new ModelAndView("insert"); // The name of your HTML file (without the .html extension)
//        modelAndView.addObject("user", new UserDTO()); // Assuming you have a User class
//        return modelAndView;
//    }
    /**
     * Creates a new user account.
     *
     * @param user The user details to be used for account creation.
     *                The request body should be a valid UserDTO object.
     * @return A response entity with the created user's ID upon successful creation,
     *         including a CREATED status code.
     */
    @PostMapping("/insert")
    public ModelAndView insertUser(@ModelAttribute("user") UserDetailsDTO user) {
        String userID = userService.insert(user);
        ModelAndView mav = new ModelAndView("redirect:/user/get");
//        mav.setViewName("AdminPost");
        return mav;
    }


    /**
     * Retrieves the details of a specific user by their unique identifier.
     *
     * @param userId The unique identifier of the user to retrieve.
     * @return A response entity containing the UserDetailsDTO object for the retrieved user
     *         upon successful retrieval, including an OK status code.
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<UserDetailsDTO> getUser(@PathVariable("id") String userId) {
        UserDetailsDTO userDto = userService.findUserById(userId);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }





    /**
     * Deletes a user account identified by its unique identifier.
     *
     * @param userId The unique identifier of the user account to be deleted.
     * @return An empty response entity upon successful deletion, including a NO_CONTENT status code.
     */
    @PostMapping("/delete/{id}")
    public ModelAndView deleteUser(@PathVariable("id") String userId, RedirectAttributes redirectAttributes) {
        String string = userService.deleteUserById(userId);
        redirectAttributes.addFlashAttribute("message", "User(" + string +") deleted successfully");
        ModelAndView mav = new ModelAndView("redirect:/user/get");
        return mav;
    }


    /**
     * Retrieves the details of a specific user by their name.
     *
     * @param name The part of the name of the users to be retrieved.
     * @return A response entity containing the UserDetailsDTO object list for the retrieved users
     *         upon successful retrieval, including an OK status code.
     */
    @GetMapping("/filter/{name}")
    public ResponseEntity<List<UserDetailsDTO>> getUserByName(@PathVariable("name") String name) {
        List<UserDetailsDTO> dtos = userService.findUserFirstNameOrLastName(name, name);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }



    /**
     * Updates the details of an existing user account.
     *
     * @param userId The unique identifier of the user account to be updated.
     * @param userDTO The updated user details to be applied.
     *                The request body should be a valid UserDetailsDTO object.
     * @return A response entity containing the updated UserDetailsDTO object upon successful update,
     *         including an OK status code.
     */
    @PostMapping("/update/{id}")
    public ModelAndView updateUser(@PathVariable("id") String userId, @ModelAttribute("user") UserDetailsDTO userDTO, RedirectAttributes redirectAttributes) {
        String msg = userService.updateUserById(userId, userDTO);
        redirectAttributes.addFlashAttribute("message", msg);
        ModelAndView mav = new ModelAndView("redirect:/user/get");
        return mav;
    }


}
