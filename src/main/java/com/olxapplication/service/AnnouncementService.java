package com.olxapplication.service;

import com.olxapplication.dtos.AnnouncementDTO;
import com.olxapplication.dtos.AnnouncementDetailsDTO;
import com.olxapplication.exception.ResourceNotFoundException;
import com.olxapplication.mappers.AnnouncementMapper;
import com.olxapplication.entity.Announcement;
import com.olxapplication.mappers.CategoryMapper;
import com.olxapplication.mappers.UserMapper;
import com.olxapplication.repository.AnnouncementRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This service layer class provides business logic operations for managing announcements within the application.
 * It interacts with the AnnouncementRepository to perform CRUD operations and leverages mappers for DTO conversions.
 */
@Service
@AllArgsConstructor
public class AnnouncementService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnouncementService.class);
    private final AnnouncementRepository announcementRepository;

    /**
     * Retrieves a list of all announcement details available in the system.
     *
     * @return A list of AnnouncementDetailsDTO objects representing all announcements.
     */
    public List<AnnouncementDetailsDTO> findAnnounces(){
        List<Announcement> announcementList = announcementRepository.findAll();
        return announcementList.stream()
                .map(AnnouncementMapper::toAnnouncementDetailsDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the details of a specific announcement identified by its unique String.
     *
     * @param id The unique identifier of the announcement to retrieve.
     * @return An AnnouncementDetailsDTO object representing the requested announcement, or throws an exception if not found.
     * @throws ResourceNotFoundException If no announcement with the provided ID exists.
     */
    public AnnouncementDetailsDTO findAnnouncementById(String id){
        Optional<Announcement> announcementOptional = announcementRepository.findById(id);
        if (!announcementOptional.isPresent()){
            LOGGER.debug("Announcement with id {" + id + "} was not found in db");
            throw new ResourceNotFoundException(Announcement.class.getSimpleName() + "with id: " + id);
        }
        return AnnouncementMapper.toAnnouncementDetailsDTO(announcementOptional.get());
    }

    /**
     * Retrieves the details of a specific announcement identified by its category id.
     *
     * @param category_id The id of the category of the announcement to retrieve.
     * @return An AnnouncementDetailsDTO object list the announcement with corresponding category id.
     */
    public List<AnnouncementDetailsDTO> findAnnouncementByCategoryId(String category_id){
        List<Announcement> announces = announcementRepository.findAnnouncementsByCategory_Id(category_id);

        return announces.stream()
                .map(AnnouncementMapper::toAnnouncementDetailsDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the details of a specific announcement identified by its user id.
     *
     * @param user_id The id of the user of the announcement to retrieve.
     * @return An AnnouncementDetailsDTO object list the announcement with corresponding user id.
     */
    public List<AnnouncementDetailsDTO> findAnnouncementByUserId(String user_id){
        List<Announcement> announces = announcementRepository.findAnnouncementsByUser_Id(user_id);

        return announces.stream()
                .map(AnnouncementMapper::toAnnouncementDetailsDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the details of a specific announcement identified by a part of its title.
     *
     * @param title The title of the user of the announcement to retrieve.
     * @return An AnnouncementDetailsDTO object list the announcement with corresponding title.
     */
    public List<AnnouncementDetailsDTO> findAnnouncementByTitle(String title){
        List<Announcement> announces = announcementRepository.findAnnouncementsByTitleContainsIgnoreCase(title);

        return announces.stream()
                .map(AnnouncementMapper::toAnnouncementDetailsDTO)
                .collect(Collectors.toList());
    }


    /**
     * Creates a new announcement in the system based on the provided details.
     *
     * @param announcementDTO An AnnouncementDetailsDTO object containing the data for the new announcement.
     * @return The String of the newly created announcement.
     */
    public String insert(AnnouncementDetailsDTO announcementDTO) {
        Announcement announcement = AnnouncementMapper.toEntity(announcementDTO);
        announcement = announcementRepository.save(announcement);
        LOGGER.debug("Announcement with id {} was inserted in db", announcement.getId());
        return announcement.getId();
    }

    /**
     * Deletes an announcement identified by its unique String.
     *
     * @param id The unique identifier of the announcement to be deleted.
     * @return The String of the deleted announcement, or throws an exception if not found.
     * @throws ResourceNotFoundException If no announcement with the provided ID exists.
     */
    public String deleteAnnouncementById(String id) {
        Optional<Announcement> announcementOptional = announcementRepository.findById(id);
        if (!announcementOptional.isPresent()) {
            LOGGER.error("Announcement with id {} was not found in db", id);
        } else {
            announcementRepository.delete(announcementOptional.get());
            LOGGER.debug("Announcement with id {} was successfully deleted", id);

        }
        return announcementOptional.get().getId();
    }

    /**
     * Updates the details of an existing announcement identified by its unique String.
     *
     * @param id The unique identifier of the announcement to be updated.
     * @param announcementDTO An AnnouncementDetailsDTO object containing the updated announcement data.
     * @return An AnnouncementDTO object representing the updated announcement, or throws an exception if not found.
     * @throws ResourceNotFoundException If no announcement with the provided ID exists.
     */
    public AnnouncementDTO updateAnnouncementById(String id, AnnouncementDetailsDTO announcementDTO) {
        Optional<Announcement> announcementOptional = announcementRepository.findById(id);

        if (!announcementOptional.isPresent()) {
            LOGGER.error("Announcement with id {} was not found in db", id);
        } else {
            Announcement toBeUpdated = announcementOptional.get();
            toBeUpdated.setTitle(announcementDTO.getTitle());
            toBeUpdated.setDescription(announcementDTO.getDescription());
            toBeUpdated.setPrice(announcementDTO.getPrice());
            toBeUpdated.setUser(UserMapper.toEntity(announcementDTO.getUser()));
            toBeUpdated.setCategory(CategoryMapper.toEntity(announcementDTO.getCategory()));
            announcementRepository.save(toBeUpdated);
            LOGGER.debug("Announcement with id {} was successfully updated", id);
        }
        return AnnouncementMapper.toAnnouncementDTO(announcementOptional.get());
    }
}
