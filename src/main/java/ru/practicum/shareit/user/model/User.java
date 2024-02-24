package ru.practicum.shareit.user.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class User {
    private long id;
    private String name;
    @NotEmpty
    @Email
    private String email;
}
