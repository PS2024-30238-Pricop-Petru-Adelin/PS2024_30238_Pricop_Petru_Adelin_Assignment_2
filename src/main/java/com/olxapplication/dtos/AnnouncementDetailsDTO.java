package com.olxapplication.dtos;

import com.olxapplication.entity.Category;
import lombok.*;


/**
 * This Data Transfer Object (DTO) encapsulates detailed information about an announcement.
 * It's primarily used for data exchange between application layers and APIs.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementDetailsDTO {
    private String id;
    private String title;
    private String description;
    private Double price;
    private UserDetailsDTO user;
    private CategoryDetailsDTO category;
}
