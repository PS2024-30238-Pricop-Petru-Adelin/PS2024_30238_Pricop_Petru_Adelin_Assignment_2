package com.olxapplication.dtos;

import com.olxapplication.entity.Announcement;
import com.olxapplication.entity.RoleType;
import lombok.*;

import java.util.List;

/**
 * This Data Transfer Object (DTO) encapsulates detailed information about an user.
 * It's primarily used for data exchange between application layers and APIs.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
    private List<String> announces;
}
