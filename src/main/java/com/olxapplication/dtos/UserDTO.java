package com.olxapplication.dtos;

import com.olxapplication.entity.Announcement;
import lombok.*;

import java.util.List;


/**
 * This Data Transfer Object (DTO) encapsulates detailed information about an user.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<Announcement> announces;
}
