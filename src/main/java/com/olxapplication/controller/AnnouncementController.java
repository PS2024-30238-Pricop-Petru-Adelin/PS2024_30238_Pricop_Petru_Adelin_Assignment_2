package com.olxapplication.controller;

import com.olxapplication.dtos.AnnouncementDTO;
import com.olxapplication.dtos.AnnouncementDetailsDTO;
import com.olxapplication.service.AnnouncementService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The controller class for managing announcements.
 * Handles HTTP requests related to announcements, interacting with a JPA data layer.
 */
@RestController
@CrossOrigin
@RequestMapping(value = "/announcement")
@Setter
@Getter
@AllArgsConstructor
@Validated
@Slf4j
public class AnnouncementController {
    private final AnnouncementService announcementService;

    /**
     * Retrieves a list of all available announcements.
     *
     * @return A response entity containing a list of AnnouncementDetailsDTO objects representing all announcements.
     */
    @GetMapping()
    public ResponseEntity<List<AnnouncementDetailsDTO>> getAnnounces(){
        List<AnnouncementDetailsDTO> dtos = announcementService.findAnnounces();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
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
    @PostMapping()
    public ResponseEntity<String> insertAnnouncement(@RequestBody AnnouncementDetailsDTO announcementDTO) {
        String announcementId = announcementService.insert(announcementDTO);
        return new ResponseEntity<>(announcementId, HttpStatus.CREATED);
    }

    /**
     * Retrieves a specific announcement by its unique identifier.
     *
     * @param announcementId The unique identifier of the announcement to retrieve.
     * @return A response entity containing the AnnouncementDetailsDTO object for the retrieved announcement
     *         upon successful retrieval, including an OK status code.
     */
    @GetMapping("/{id}")
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
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAnnouncement(@PathVariable("id") String announcementId) {
        String String = announcementService.deleteAnnouncementById(announcementId);
        return new ResponseEntity<>(String, HttpStatus.OK);
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
    @PutMapping("/{id}")
    public ResponseEntity<AnnouncementDTO> updateAnnouncement(@PathVariable("id") String announcementId, @RequestBody AnnouncementDetailsDTO announcementDTO) {
        AnnouncementDTO announcement = announcementService.updateAnnouncementById(announcementId, announcementDTO);
        return new ResponseEntity<>(announcement, HttpStatus.OK);
    }
}
