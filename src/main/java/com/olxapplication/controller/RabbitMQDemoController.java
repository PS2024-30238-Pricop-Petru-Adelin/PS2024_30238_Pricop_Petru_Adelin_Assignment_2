package com.olxapplication.controller;

import com.olxapplication.dtos.UserDetailsDTO;
import com.olxapplication.entity.Mail;
import com.olxapplication.config.RabbitMQSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/rabbitmq")
public class RabbitMQDemoController {
    @Autowired
    RabbitMQSender rabbitMQSender;
    @PostMapping(value = "/sender/update")
    public ModelAndView producerUpdate(@ModelAttribute("message") UserDetailsDTO userDetailsDTO) {
        rabbitMQSender.send(new Mail(userDetailsDTO.getEmail(),
                "USER UPDATE",
                "Your user details have been updated successfully: " + userDetailsDTO.getFirstName() + " " + userDetailsDTO.getLastName()).toString());
        System.out.println("Message sent to the RabbitMQ Queue Successfully");
        ModelAndView mav = new ModelAndView("redirect:/user/get");
        return mav;
    }
    @PostMapping(value = "/sender/insert")
    public ModelAndView producerInsert(@ModelAttribute("message") UserDetailsDTO userDetailsDTO) {
        rabbitMQSender.send(new Mail(userDetailsDTO.getEmail(),
                "Successfull registration",
                "You are now part of our comunity, " + userDetailsDTO.getFirstName() + " " + userDetailsDTO.getLastName()).toString());
        System.out.println("Message sent to the RabbitMQ Queue Successfully");
        ModelAndView mav = new ModelAndView("redirect:/user/get");
        return mav;
    }
}
