package com.olxapplication.controller;

import com.olxapplication.dtos.AnnouncementDTO;
import com.olxapplication.dtos.AnnouncementDetailsDTO;
import com.olxapplication.dtos.AnnouncementWebDTO;
import com.olxapplication.mappers.CategoryMapper;
import com.olxapplication.mappers.UserMapper;
import com.olxapplication.service.AnnouncementService;
import com.olxapplication.service.CategoryService;
import com.olxapplication.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * The controller class for managing announcements.
 * Handles HTTP requests related to announcements, interacting with a JPA data layer.
 */
@Controller
@CrossOrigin
@RequestMapping(value = "/announcement")
@Setter
@Getter
@AllArgsConstructor
@Validated
@Slf4j
public class AnnouncementController {
    private final AnnouncementService announcementService;
    private final CategoryController categoryController;
    private final UserController userController;
    private final UserService userService;
    private final CategoryService categoryService;

    /**
     * Retrieves a list of all available announcements.
     *
     * @return A response entity containing a list of AnnouncementDetailsDTO objects representing all announcements.
     */
    @GetMapping("/get")
    public ModelAndView getAnnounces(){
        List<AnnouncementDetailsDTO> dtos = announcementService.findAnnounces();
        ModelAndView mav = new ModelAndView("AdminGetAnnounces");
        mav.addObject("announces", dtos);
        return mav;
    }

    /**
     * Retrieves a list of available announces with the specified category id.
     *
     * @param category_id The unique identifier of the category of announcement to be retrieved.
     * @return A response entity containing the AnnouncementDetailsDTO object list  for the retrieved announcements
     *         upon successful retrieval, including an OK status code.
     */
    @GetMapping("/byCategory/{category_id}")
    public ResponseEntity<List<AnnouncementDetailsDTO>> getAnnouncesByCategoryId(@PathVariable("category_id") String category_id){
        List<AnnouncementDetailsDTO> dtos = announcementService.findAnnouncementByCategoryId(category_id);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /**
     * Retrieves a list of available announces with the specified user id.
     *
     * @param user_id The unique identifier of the user of announcement to be retrieved.
     * @return A response entity containing the AnnouncementDetailsDTO object list for the retrieved announcements
     *         upon successful retrieval, including an OK status code.
     */
    @GetMapping("/byUser/{user_id}")
    public ResponseEntity<List<AnnouncementDetailsDTO>> getAnnouncesByUserId(@PathVariable("user_id") String user_id){
        List<AnnouncementDetailsDTO> dtos = announcementService.findAnnouncementByUserId(user_id);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /**
     * Retrieves a list of available announces with the specified user id.
     *
     * @param title The unique identifier of the user of announcement to be retrieved.
     * @return A response entity containing the AnnouncementDetailsDTO object list for the retrieved announcements
     *         upon successful retrieval, including an OK status code.
     */
    @GetMapping("/byTitle/{title}")
    public ResponseEntity<List<AnnouncementDetailsDTO>> getAnnouncesByTitle(@PathVariable("title") String title){
        List<AnnouncementDetailsDTO> dtos = announcementService.findAnnouncementByTitle(title);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /**
     * Creates a new announcement.
     *
     * @param announcementDTO The announcement details to be used for creation.
     *                        The request body should be a valid AnnouncementDetailsDTO object.
     * @return A response entity with the created announcement's ID upon successful creation,
     *         including a CREATED status code.
     */
    @PostMapping("/insert")
    public ModelAndView insertAnnouncement(@ModelAttribute("announcement") AnnouncementWebDTO announcementDTO) {

        String announcementId = announcementService.insert(announcementDTO);
        ModelAndView mav = new ModelAndView("redirect:/announcement/get");
        return mav;
    }

    /**
     * Retrieves a specific announcement by its unique identifier.
     *
     * @param announcementId The unique identifier of the announcement to retrieve.
     * @return A response entity containing the AnnouncementDetailsDTO object for the retrieved announcement
     *         upon successful retrieval, including an OK status code.
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<AnnouncementDetailsDTO> getAnnouncement(@PathVariable("id") String announcementId) {
        AnnouncementDetailsDTO announcementDetailsDTO = announcementService.findAnnouncementById(announcementId);
        return new ResponseEntity<>(announcementDetailsDTO, HttpStatus.OK);
    }

    /**
     * Deletes an announcement identified by its unique identifier.
     *
     * @param announcementId The unique identifier of the announcement to be deleted.
     * @return An String response entity upon successful deletion, including a NO_CONTENT status code.
     */
    @PostMapping("/delete/{id}")
    public ModelAndView deleteAnnouncement(@PathVariable("id") String announcementId, RedirectAttributes redirectAttributes) {
        String string = announcementService.deleteAnnouncementById(announcementId);
        ModelAndView mav = new ModelAndView("redirect:/announcement/get");
        redirectAttributes.addFlashAttribute("message", "Announcement (" + string +") deleted successfully");
        return mav;
    }

    /**
     * Updates an existing announcement with the provided details.
     *
     * @param announcementId The unique identifier of the announcement to be updated.
     * @param announcementDTO The announcement details containing the updated information.
     *                        The request body should be a valid AnnouncementDTO object.
     * @return A response entity containing the updated AnnouncementDetailsDTO object upon successful update,
     *         including an OK status code.
     */
    @PostMapping("/update/{id}")
    public ModelAndView updateAnnouncement(@PathVariable("id") String announcementId, @ModelAttribute("announcement") AnnouncementWebDTO announcementDTO) {

        System.out.println("-"+announcementDTO.getCategory()+"-" + "     " + "-"+announcementDTO.getUser()+"-");

        AnnouncementDTO announcementUpdated = announcementService.updateAnnouncementById(announcementId, announcementDTO);
        ModelAndView mav = new ModelAndView("redirect:/announcement/get");
        return mav;
    }
}
