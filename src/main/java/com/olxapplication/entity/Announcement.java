package com.olxapplication.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;



/**
 * This entity class represents an announcement within the application's persistent data model.
 * It maps to the corresponding "announces_table" in the database.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "announces_table")
public class Announcement {
    /**
     * The unique identifier of the announcement, generated using a String strategy.
     */
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy =  "uuid2")
    private String id;

    /**
     * The title of the announcement, which concisely summarizes its content.
     */
    @Column(name = "title", nullable = false)
    private String title;

    /**
     * A detailed description providing more information about the selled product.
     */
    @Column(name = "description", nullable = false)
    private String description;

    /**
     * The price of the selled product or service.
     */
    @Column(name = "price", nullable = false)
    private Double price;

    /**
     * The user who created the announcement, establishing a many-to-one relationship with the User entity.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * The category to which the announcement belongs, defining a many-to-one relationship with the Category entity.
     */
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
