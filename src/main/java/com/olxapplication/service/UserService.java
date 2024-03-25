package com.olxapplication.service;

import com.olxapplication.dtos.UserDetailsDTO;
import com.olxapplication.exception.ResourceNotFoundException;
import com.olxapplication.mappers.UserMapper;
import com.olxapplication.entity.User;
import com.olxapplication.repository.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This service layer class provides business logic operations for managing users within the application.
 * It interacts with the UserRepository to perform CRUD operations and leverages mappers for DTO conversions.
 */
@Service
@AllArgsConstructor
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    /**
     * Retrieves a list of all user details available in the system.
     *
     * @return A list of UserDetailsDTO objects representing all users.
     */
    public List<UserDetailsDTO> findUsers(){
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(UserMapper::toUserDetailsDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the details of a specific user identified by their unique String.
     *
     * @param id The unique identifier of the user to retrieve.
     * @return A UserDetailsDTO object representing the requested user, or throws an exception if not found.
     * @throws ResourceNotFoundException If no user with the provided ID exists.
     */
    public UserDetailsDTO findUserById(String id){
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()){
            LOGGER.debug("User with id {" + id + "} was not found in db");
            throw new ResourceNotFoundException(User.class.getSimpleName() + "with id: " + id);
        }
        return UserMapper.toUserDetailsDTO(userOptional.get());
    }

    /**
     * Retrieves the details of a specific user identified by their name.
     *
     * @param str  The part of the name of the user to retrieve.
     * @param name
     * @return A UserDetailsDTO object list representing the requested users.
     */
    public List<UserDetailsDTO> findUserFirstNameOrLastName(String str, String name){
        List<User> userList = userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(str, str);
        return userList.stream()
                .map(UserMapper::toUserDetailsDTO)
                .collect(Collectors.toList());
    }

    /**
     * Creates a new user in the system based on the provided details.
     *
     * @param userDTO A UserDetailsDTO object containing the data for the new user.
     * @return The String of the newly created user.
     */
    public String insert(UserDetailsDTO userDTO) {
        User user = UserMapper.toEntity(userDTO);
        user = userRepository.save(user);
        LOGGER.debug("User with id {} was inserted in db", user.getId());
        return user.getId();
    }

    /**
     * Deletes a user identified by their unique String.
     *
     * @param id The unique identifier of the user to be deleted.
     * @return The String of the deleted user, or throws an exception if not found.
     * @throws ResourceNotFoundException If no user with the provided ID exists.
     */
    public String deleteUserById(String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            LOGGER.error("User with id {} was not found in db", id);
        } else {
            userRepository.delete(userOptional.get());
            LOGGER.debug("User with id {} was successfully deleted", id);

        }
        return userOptional.get().getId();
    }

    /**
     * Updates the details of an existing user identified by their unique String.
     *
     * @param id The unique identifier of the user to be updated.
     * @param userDTO A UserDetailsDTO object containing the updated user data.
     * @return A UserDetailsDTO object representing the updated user, or throws an exception if not found.
     * @throws ResourceNotFoundException If no user with the provided ID exists.
     */
    public UserDetailsDTO updateUserById(String id, UserDetailsDTO userDTO) {
        Optional<User> userOptional = userRepository.findById(id);
        
        if (!userOptional.isPresent()) {
            LOGGER.error("User with id {} was not found in db", id);
        } else {
            User toBeUpdated = userOptional.get();
            toBeUpdated.setFirstName(userDTO.getFirstName());
            toBeUpdated.setLastName(userDTO.getLastName());
            toBeUpdated.setEmail(userDTO.getEmail());
            toBeUpdated.setPassword(userDTO.getPassword());
//            toBeUpdated.setAnnounces(userDTO.getAnnounces());
            userRepository.save(toBeUpdated);
            LOGGER.debug("User with id {} was successfully updated", id);
        }
        return UserMapper.toUserDetailsDTO(userOptional.get());
    }


}
