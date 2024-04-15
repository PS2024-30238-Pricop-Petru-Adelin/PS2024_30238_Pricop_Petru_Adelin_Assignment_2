package com.olxapplication.controller;

import com.olxapplication.dtos.AnnouncementDetailsDTO;
import com.olxapplication.dtos.AnnouncementWebDTO;
import com.olxapplication.dtos.MessageWebDTO;
import com.olxapplication.entity.Message;
import com.olxapplication.entity.User;
import com.olxapplication.service.MessageService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@CrossOrigin
@RequestMapping(value = "/message")
@Setter
@Getter
@AllArgsConstructor
@Validated
@Slf4j
public class MessageController {
    private final MessageService messageService;

    @GetMapping("/sent/{id}")
    public ModelAndView getSentMessages(@PathVariable("id") String id){
        List<Message> messages = messageService.findSent(id);
        ModelAndView mav = new ModelAndView("redirect:/announcement/getMine" + id);
        mav.addObject("msgs", messages);
        return mav;
    }

    @GetMapping("/received/{id}")
    public ModelAndView getReceivedMessages(@PathVariable("id") String id){
        List<Message> messages = messageService.findReceived(id);
        ModelAndView mav = new ModelAndView("redirect:/announcement/getMine/" + id);
        mav.addObject("msgs", messages);
        return mav;
    }

    @PostMapping("/send")
    public ModelAndView sendMessage(@ModelAttribute("msg") MessageWebDTO messageWebDTO, RedirectAttributes redirectAttributes) {

        String msg = messageService.insert(messageWebDTO);
        ModelAndView mav = new ModelAndView("redirect:/message/chat/" + messageWebDTO.getSender() + "/" + messageWebDTO.getReceiver());
        redirectAttributes.addFlashAttribute("message", msg);
        return mav;
    }

    @GetMapping("/messages/{id}")
    public ModelAndView getCorespondents(@PathVariable("id") String id){
        List<User> users = messageService.findCorespondents(id);
        ModelAndView mav = new ModelAndView("Corespondents");
        mav.addObject("users", users);
        return mav;
    }

    @GetMapping("/chat/{id}/{corespondentId}")
    public ModelAndView getChat(@PathVariable("id") String id, @PathVariable("corespondentId") String corespondentId){
        List<Message> messages = messageService.findChat(id, corespondentId);
        ModelAndView mav = new ModelAndView("ChatNou");
        mav.addObject("msgs", messages);
        return mav;
    }
}
