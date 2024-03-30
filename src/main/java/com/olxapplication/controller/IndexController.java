package com.olxapplication.controller;

import com.olxapplication.dtos.UserDetailsDTO;
import com.olxapplication.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


/**
 * Controller for handling requests to the index page.
 */
@Controller
@CrossOrigin
@RequestMapping(value = "/index")
@Setter
@Getter
@AllArgsConstructor
public class IndexController {
    private final UserService userService;


    /**
     * Handles GET requests to the HomePage -- The future log in page.
     * @return ModelAndView object with a list of users for log in.
     */
    @GetMapping("/HomePage")
    public ModelAndView logIn(){
        List<UserDetailsDTO> users = userService.findUsers();
        ModelAndView mav = new ModelAndView("HomePage");
        mav.addObject("users", users);
        return mav;
    }

    /**
     * Handles POST requests to redirect the user based on their role.
     * @param userId The ID of the user.
     * @return ModelAndView object with the view name set based on the user's role.
     */
    @PostMapping("/redirectPage")
    public ModelAndView redirectBasedOnRole(@ModelAttribute("userId") String userId){
        ModelAndView mav = new ModelAndView();
        if(userService.findUserById(userId).getRole().equals("admin")) {
            mav.setViewName("redirect:/user/get");
        } else {
            mav.setViewName("redirect:/announcement/getOthers/" + userId); // Redirect to a valid endpoint for non-admin users
        }
        return mav;
    }

    /**
     * Handles POST requests to redirect the admin to user management page.
     * @param newLink The new link to redirect to(link to user management page send from frontend).
     * @return ModelAndView object with the view name set to the new link.
     */
    @PostMapping("/admin")
    public ModelAndView adminRedirect(@ModelAttribute("newLink") String newLink){
        System.out.println("-"+newLink+"-");
        ModelAndView mav = new ModelAndView(newLink);
        return mav;
    }

    /**
     * Handles POST requests to redirect the user to the listed announces.
     * @param id The ID of the user.
     * @param newLink The new link to redirect to(see announces page).
     * @return ModelAndView object with the view name set to the new link.
     */
    @PostMapping("/user/{id}")
    public ModelAndView userRedirect(@PathVariable("id") String id, @ModelAttribute("newLink") String newLink){
        System.out.println("-"+newLink+"-");
        ModelAndView mav = new ModelAndView(newLink);
        return mav;
    }

}
