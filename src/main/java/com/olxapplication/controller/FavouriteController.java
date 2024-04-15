package com.olxapplication.controller;

import com.olxapplication.dtos.UserDetailsDTO;
import com.olxapplication.entity.Announcement;
import com.olxapplication.entity.Favourite;
import com.olxapplication.service.FavouriteService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@CrossOrigin
@RequestMapping(value = "/favourite")
@Setter
@Getter
@AllArgsConstructor
public class FavouriteController {
    private final FavouriteService favouriteService;

    @GetMapping("/get/{id}")
    public ModelAndView getAnnounces(@PathVariable("id") String userId) {
        ModelAndView mav = new ModelAndView("Favourites");
        Favourite favourite = favouriteService.findByUserId(userId);
        mav.addObject("favourite", favourite);
        return mav;
    }

    @PostMapping("/add/{id}/{announcementId}")
    public ModelAndView addAnnouncementToFavourites(@PathVariable("id") String userId, @PathVariable("announcementId") String announcementId,  RedirectAttributes redirectAttributes) {
        String msg = favouriteService.insertAnnouncement(userId, announcementId);
        redirectAttributes.addFlashAttribute("message", msg);
        ModelAndView mav = new ModelAndView("redirect:/favourite/get/{id}");
        return mav;
    }

    @PostMapping("/remove/{id}/{announcementId}")
    public ModelAndView removeAnnouncementFromFavourites(@PathVariable("id") String userId,@PathVariable("announcementId") String announcementId,  RedirectAttributes redirectAttributes) {
        String msg = favouriteService.deleteAnnouncement(userId, announcementId);
        redirectAttributes.addFlashAttribute("message", msg);
        ModelAndView mav = new ModelAndView("redirect:/favourite/get/{id}");
        return mav;
    }
}
