package com.olxapplication.service;

import com.olxapplication.constants.AnnouncementMessages;
import com.olxapplication.constants.FavouriteMessages;
import com.olxapplication.constants.UserMessages;
import com.olxapplication.entity.Announcement;
import com.olxapplication.entity.Favourite;
import com.olxapplication.exception.ResourceNotFoundException;
import com.olxapplication.mappers.UserMapper;
import com.olxapplication.repository.AnnouncementRepository;
import com.olxapplication.repository.FavouriteRepository;
import com.olxapplication.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FavouriteService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FavouriteService.class);
    private final FavouriteRepository favouriteRepository;
    private final AnnouncementRepository announcementRepository;

    public Double computeTotal(Favourite favourite) {
        double total = 0.0;
        for(Announcement a : favourite.getFavouriteAnnouncements()){
            total += a.getNewPrice();
        }
        return ((int)(total*100))/100.0;
    }

    public Favourite findById(String id) {
        Optional<Favourite> favourite = favouriteRepository.findById(id);
        if(favourite.isEmpty()) {
            LOGGER.debug(FavouriteMessages.FAVOURITE_NOT_FOUND + id);
            throw new ResourceNotFoundException(FavouriteMessages.FAVOURITE_NOT_FOUND + id);
        }
        return favourite.get();
    }

    public Favourite findByUserId(String userId) {
        Optional<Favourite> favourite = favouriteRepository.findByUserId(userId);
        if(favourite.isEmpty()) {
            LOGGER.debug(FavouriteMessages.FAVOURITE_NOT_FOUND + UserMessages.USER_NOT_FOUND + userId);
            throw new ResourceNotFoundException(UserMessages.USER_NOT_FOUND + userId);
        }
        favourite.get().setTotal(computeTotal(favourite.get()));
        favouriteRepository.save(favourite.get());
        return favourite.get();
    }

    public String insertAnnouncement(String userId, String announcementId) {
        Favourite favourite = findByUserId(userId);
        if(favourite == null) {
            LOGGER.debug(FavouriteMessages.FAVOURITE_NOT_FOUND + UserMessages.USER_NOT_FOUND + userId);
            return FavouriteMessages.FAVOURITE_NOT_FOUND + UserMessages.USER_NOT_FOUND + userId;
        }

        for(Announcement a : favourite.getFavouriteAnnouncements()){
            if(a.getId().equals(announcementId)){
                LOGGER.debug(FavouriteMessages.ANNOUNCEMNT_ALREADY_ADDED + announcementId);
                return FavouriteMessages.ANNOUNCEMNT_ALREADY_ADDED + announcementId;
            }
        }

        Optional<Announcement> announcement = announcementRepository.findById(announcementId);
        if(announcement.isEmpty()) {
            LOGGER.debug(FavouriteMessages.FAVOURITE_NOT_ADDED + AnnouncementMessages.ANNOUNCEMENT_NOT_FOUND + announcementId);
            return FavouriteMessages.FAVOURITE_NOT_ADDED + AnnouncementMessages.ANNOUNCEMENT_NOT_FOUND + announcementId;
        }


        favourite.getFavouriteAnnouncements().add(announcement.get());
        favourite.setTotal(computeTotal(favourite));
        favouriteRepository.save(favourite);
        LOGGER.debug(FavouriteMessages.FAVOURITE_ADDED_SUCCESSFULLY);

        return FavouriteMessages.FAVOURITE_ADDED_SUCCESSFULLY;
    }

    public String deleteAnnouncement(String userId, String announcementId) {
        Favourite favourite = findByUserId(userId);
        if(favourite == null) {
            LOGGER.debug(FavouriteMessages.FAVOURITE_NOT_FOUND + UserMessages.USER_NOT_FOUND + userId);
            return FavouriteMessages.FAVOURITE_NOT_FOUND + UserMessages.USER_NOT_FOUND + userId;
        }
        for(int i = favourite.getFavouriteAnnouncements().size() - 1; i >= 0; i--) {
            if(favourite.getFavouriteAnnouncements().get(i).getId().equals(announcementId)){

                favourite.getFavouriteAnnouncements().remove(i);
                favourite.setTotal(computeTotal(favourite));
                favouriteRepository.save(favourite);

                LOGGER.debug(FavouriteMessages.FAVOURITE_REMOVED_SUCCESSFULLY);
                return FavouriteMessages.FAVOURITE_REMOVED_SUCCESSFULLY;
            }
        }


        LOGGER.debug(FavouriteMessages.FAVOURITE_NOT_REMOVED);
        return FavouriteMessages.FAVOURITE_NOT_REMOVED;
    }
}
