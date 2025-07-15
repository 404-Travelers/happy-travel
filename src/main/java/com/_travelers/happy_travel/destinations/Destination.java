package com._travelers.happy_travel.destinations;

import com._travelers.happy_travel.users.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table (name = "destinations")
public class Destination {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String country;

    @Column (nullable = false)
    private String city;

    @Column (nullable = false)
    private String description;

    @Column (name = "image_url")
    private String imageUrl;

    @ManyToOne
    private User user;

    public Destination(@NotNull(message = "Country is required") @Size(max = 50, message = "Country must be less than 50 characters") String country, @NotNull(message = "City is required") @Size(max = 50, message = "City must be less than 50 characters") String city, @Size(max = 255, message = "Description must be less than 255 characters") String description, @Pattern(message = "Invalid content type", regexp = "^(https?://.*\\.(png|jpg|jpeg|gif|svg))$") String s, User user) {
    }

    public Destination(Long id, String country, String city, String description, String imageUrl, User user) {
        this.id = id;
        this.country = country;
        this.city = city;
        this.description = description;
        this.imageUrl = imageUrl;
        this.user = user;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
