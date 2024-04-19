package com.olxapplication.dtos;

import lombok.*;

/**
 * This Data Transfer Object (DTO) encapsulates detailed information about an user.
 * It's primarily used for data exchange between application layers and APIs.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserMailDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
}
