package com.olxapplication.controller;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.olxapplication.config.RabbitMQSender;
import com.olxapplication.dtos.*;
import com.olxapplication.service.UserService;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
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
    private static final String URL = "http://localhost:8081/microservice/receiver";
    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private final RabbitMQSender rabbitMQSender;

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
//    @PostMapping("/insert")
//    public ModelAndView insertUser(@ModelAttribute("user") UserDetailsDTO user, RedirectAttributes redirectAttributes) {
//        String msg = userService.insert(user);
//        ModelAndView mav = new ModelAndView("Intermediate");
//        mav.addObject("user", user);
////        ModelAndView mav = new ModelAndView("redirect:/user/get");
//        redirectAttributes.addFlashAttribute("message", msg);
//        return mav;
//    }

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

//    /**
//     * Update a user by its ID.
//     * @param userId The ID of the user.
//     * @param userDTO The user details.
//     * @param redirectAttributes Redirect attributes.
//     * @return ModelAndView for redirection.
//     */
//    @PostMapping("/update/{id}")
//    public ModelAndView updateUser(@PathVariable("id") String userId, @ModelAttribute("user") UserDetailsDTO userDTO, RedirectAttributes redirectAttributes) {
//        String msg = userService.updateUserById(userId, userDTO);
//        redirectAttributes.addFlashAttribute("message", msg);
//        ModelAndView mav = new ModelAndView("Intermediate");
//        mav.addObject("user", userDTO);
////        ModelAndView mav = new ModelAndView("redirect:/user/get");
//        return mav;
//    }

    @PostMapping("/insert")
    public ModelAndView insertUser(@ModelAttribute("user") UserDetailsDTO userDetailsDTO, RedirectAttributes redirectAttributes) {
        String msg = userService.insert(userDetailsDTO);

        // Crearea unui nou UserDto și trimiterea acestuia în coadă
        UserMailDTO userDto = new UserMailDTO(userDetailsDTO.getId()
                , userDetailsDTO.getFirstName()
                , userDetailsDTO.getLastName()
                , userDetailsDTO.getEmail()
                , "insert", "");
//        rabbitMQSender.send(userDto);

        // Crearea HttpHeaders și setarea token-ului
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(userDto.getId() + userDto.getEmail()); // presupunem că token-ul este disponibil

        // Crearea NotificationRequestDto și HttpEntity
        NotificationRequestDto notificationRequestDto = new NotificationRequestDto(userDto.getId(), userDto.getFirstName() + " " + userDto.getLastName(), userDto.getEmail(), "insert"); // completați cu datele necesare
        HttpEntity<NotificationRequestDto> entity = new HttpEntity<>(notificationRequestDto, headers);

        // Apelarea metodei restTemplate.exchange
        ResponseMessageDto response = restTemplate.exchange(URL, HttpMethod.POST, entity, ResponseMessageDto.class).getBody();
        System.out.println("!!!!!!!!------------>" + response + "<------------!!!!!!!!");

        ModelAndView mav = new ModelAndView("redirect:/user/get");
        redirectAttributes.addFlashAttribute("message", msg);
        return mav;
    }

    @PostMapping("/update/{id}")
    public ModelAndView updateUser(@PathVariable("id") String userId, @ModelAttribute("user") UserDetailsDTO userDetailsDTO, RedirectAttributes redirectAttributes) {
        String msg = userService.updateUserById(userId, userDetailsDTO);

        // Crearea unui nou UserDto și trimiterea acestuia în coadă
        UserMailDTO userDTO = new UserMailDTO(userDetailsDTO.getId()
                , userDetailsDTO.getFirstName()
                , userDetailsDTO.getLastName()
                , userDetailsDTO.getEmail()
                , "update"
                ,"");
        rabbitMQSender.send(userDTO);

//        // Crearea HttpHeaders și setarea token-ului
//        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//        headers.setBearerAuth(userDTO.getId() + userDTO.getEmail()); // presupunem că token-ul este disponibil
//
//        // Crearea NotificationRequestDto și HttpEntity
//        NotificationRequestDto notificationRequestDto = new NotificationRequestDto(userDTO.getId(), userDTO.getFirstName() + " " + userDTO.getLastName(), userDTO.getEmail(), "update"); // completați cu datele necesare
//        HttpEntity<NotificationRequestDto> entity = new HttpEntity<>(notificationRequestDto, headers);
//
//        // Apelarea metodei restTemplate.exchange
//        ResponseMessageDto response = restTemplate.exchange(URL, HttpMethod.POST, entity, ResponseMessageDto.class).getBody();
//        System.out.println("!!!!!!!!------------>" + response + "<------------!!!!!!!!");
        ModelAndView mav = new ModelAndView("redirect:/user/get");
        redirectAttributes.addFlashAttribute("message", msg);
        return mav;
    }


}

