package com.olxapplication.service;

import com.olxapplication.constants.AnnouncementMessages;
import com.olxapplication.constants.CategoryMessages;
import com.olxapplication.constants.UserMessages;
import com.olxapplication.dtos.*;
import com.olxapplication.entity.Category;
import com.olxapplication.entity.User;
import com.olxapplication.exception.PatternNotMathcedException;
import com.olxapplication.exception.ResourceNotFoundException;
import com.olxapplication.mappers.AnnouncementMapper;
import com.olxapplication.entity.Announcement;
import com.olxapplication.mappers.CategoryMapper;
import com.olxapplication.mappers.UserMapper;
import com.olxapplication.repository.AnnouncementRepository;
import com.olxapplication.repository.CategoryRepository;
import com.olxapplication.repository.UserRepository;
import com.olxapplication.validators.AnnouncementValidator;
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
    private final AnnouncementValidator announcementValidator = new AnnouncementValidator();
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

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

    public List<AnnouncementDetailsDTO> findOtherAnnounces(String id){
        List<Announcement> announcementList = announcementRepository.findAll();
        for(int i = announcementList.size()-1; i >= 0; i--){
            Announcement a = announcementList.get(i);
            if(a.getUser().getId().equals(id)){
                announcementList.remove(announcementList.get(i));
            }
        }
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

    public String insert(AnnouncementWebDTO announcementWebDTO) {
        try{

            announcementValidator.announcementWebDtoValidator(announcementWebDTO);
            Optional<User> user = userRepository.findById(announcementWebDTO.getUser());
            Optional<Category> category = categoryRepository.findById(announcementWebDTO.getCategory());
            if(user.isPresent()) {
                if(category.isPresent()) {
                    AnnouncementDetailsDTO ann = AnnouncementDetailsDTO.builder()
                            .title(announcementWebDTO.getTitle())
                            .description(announcementWebDTO.getDescription())
                            .price(announcementWebDTO.getPrice())
                            .user(UserMapper.toUserDetailsDTO(user.get()))
                            .category(CategoryMapper.toCategoryDetailsDTO(category.get()))
                            .build();
                    Announcement announcement = AnnouncementMapper.toEntity(ann);
                    announcement = announcementRepository.save(announcement);
                    LOGGER.debug(AnnouncementMessages.ANNOUNCEMENT_INSERTED_SUCCESSFULLY + announcement.getId() +  announcementWebDTO.getUser());
                    return AnnouncementMessages.ANNOUNCEMENT_INSERTED_SUCCESSFULLY + announcement.getId() + announcementWebDTO.getUser();
                } else {
                    LOGGER.error(AnnouncementMessages.ANNOUNCEMENT_NOT_INSERTED + CategoryMessages.CATEGORY_NOT_FOUND + announcementWebDTO.getCategory());
                    return AnnouncementMessages.ANNOUNCEMENT_NOT_INSERTED + CategoryMessages.CATEGORY_NOT_FOUND  + announcementWebDTO.getCategory();
                }
            } else {
                LOGGER.error(AnnouncementMessages.ANNOUNCEMENT_NOT_INSERTED + UserMessages.USER_NOT_FOUND );
                return AnnouncementMessages.ANNOUNCEMENT_NOT_INSERTED + UserMessages.USER_NOT_FOUND;
            }
        } catch (PatternNotMathcedException e){
            LOGGER.error(AnnouncementMessages.ANNOUNCEMENT_NOT_INSERTED + e.getMessage());
            return AnnouncementMessages.ANNOUNCEMENT_NOT_INSERTED + e.getMessage();
        } catch (ResourceNotFoundException e){
            LOGGER.error(AnnouncementMessages.ANNOUNCEMENT_NOT_INSERTED + e.getMessage());
            return AnnouncementMessages.ANNOUNCEMENT_NOT_INSERTED + e.getMessage();
        }

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
            LOGGER.error(AnnouncementMessages.ANNOUNCEMENT_NOT_FOUND + id);
            return AnnouncementMessages.ANNOUNCEMENT_NOT_FOUND + id;
        } else {
            announcementRepository.delete(announcementOptional.get());
            LOGGER.debug(AnnouncementMessages.ANNOUNCEMENT_DELETED_SUCCESSFULLY + id);
            return AnnouncementMessages.ANNOUNCEMENT_DELETED_SUCCESSFULLY + id;
        }

    }

    /**
     * Updates the details of an existing announcement identified by its unique String.
     *
     * @param id The unique identifier of the announcement to be updated.
     * @param announcementDTO An AnnouncementDetailsDTO object containing the updated announcement data.
     * @return An AnnouncementDTO object representing the updated announcement, or throws an exception if not found.
     * @throws ResourceNotFoundException If no announcement with the provided ID exists.
     */
    public String updateAnnouncementById(String id, AnnouncementDetailsDTO announcementDTO) {
        Optional<Announcement> announcementOptional = announcementRepository.findById(id);

        if (!announcementOptional.isPresent()) {
            LOGGER.error(AnnouncementMessages.ANNOUNCEMENT_NOT_FOUND + id);
            return AnnouncementMessages.ANNOUNCEMENT_NOT_FOUND + id;
        } else {
            Announcement toBeUpdated = announcementOptional.get();
            toBeUpdated.setTitle(announcementDTO.getTitle());
            toBeUpdated.setDescription(announcementDTO.getDescription());
            toBeUpdated.setPrice(announcementDTO.getPrice());
            toBeUpdated.setUser(UserMapper.toEntity(announcementDTO.getUser()));
            toBeUpdated.setCategory(CategoryMapper.toEntity(announcementDTO.getCategory()));
            announcementRepository.save(toBeUpdated);
            LOGGER.debug(AnnouncementMessages.ANNOUNCEMENT_UPDATED_SUCCESSFULLY + id);
            return AnnouncementMessages.ANNOUNCEMENT_UPDATED_SUCCESSFULLY + id;
        }
    }

    public String updateAnnouncementById(String id, AnnouncementWebDTO announcementWebDTO) {
        try {
            announcementValidator.announcementWebDtoValidator(announcementWebDTO);
            Optional<Announcement> announcementOptional = announcementRepository.findById(id);

            if (!announcementOptional.isPresent()) {
                LOGGER.error(AnnouncementMessages.ANNOUNCEMENT_NOT_FOUND + id);
                return AnnouncementMessages.ANNOUNCEMENT_NOT_FOUND + id;
            } else {
                Announcement toBeUpdated = announcementOptional.get();
                toBeUpdated.setTitle(announcementWebDTO.getTitle());
                toBeUpdated.setDescription(announcementWebDTO.getDescription());
                toBeUpdated.setPrice(announcementWebDTO.getPrice());
                toBeUpdated.setUser(userRepository.findById(announcementWebDTO.getUser()).get());
                toBeUpdated.setCategory(categoryRepository.findById(announcementWebDTO.getCategory()).get());
                announcementRepository.save(toBeUpdated);
                LOGGER.debug(AnnouncementMessages.ANNOUNCEMENT_UPDATED_SUCCESSFULLY + id);
                return AnnouncementMessages.ANNOUNCEMENT_UPDATED_SUCCESSFULLY + id;
            }
        } catch (PatternNotMathcedException e){
            LOGGER.error(AnnouncementMessages.ANNOUNCEMENT_UPDATED_SUCCESSFULLY + e.getMessage());
            return AnnouncementMessages.ANNOUNCEMENT_UPDATED_SUCCESSFULLY + e.getMessage();
        }
    }
}
