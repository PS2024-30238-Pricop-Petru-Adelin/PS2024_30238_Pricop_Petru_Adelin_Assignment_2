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

@Controller
@CrossOrigin
@RequestMapping(value = "/index")
@Setter
@Getter
@AllArgsConstructor
public class IndexController {
    private final UserService userService;

    @GetMapping("/HomePage")
    public ModelAndView logIn(){
        List<UserDetailsDTO> users = userService.findUsers();
        ModelAndView mav = new ModelAndView("HomePage");
        mav.addObject("users", users);
        return mav;
    }

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

    @GetMapping("/admin")
    public ModelAndView adminChoose(){
        ModelAndView mav = new ModelAndView("AdminChooseAction");
        return mav;
    }

    @PostMapping("/admin")
    public ModelAndView adminRedirect(@ModelAttribute("newLink") String newLink){
        System.out.println("-"+newLink+"-");
        ModelAndView mav = new ModelAndView(newLink);
        return mav;
    }

    @GetMapping("/user/{id}")
    public ModelAndView userChoose(@PathVariable("id") String id){
        ModelAndView mav = new ModelAndView("UserChooseAction");
        return mav;
    }

    @PostMapping("/user/{id}")
    public ModelAndView userRedirect(@PathVariable("id") String id, @ModelAttribute("newLink") String newLink){
        System.out.println("-"+newLink+"-");
        ModelAndView mav = new ModelAndView(newLink);
        return mav;
    }

}
