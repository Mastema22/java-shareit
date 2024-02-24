package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotEmpty;


/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {
    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    private boolean isAvailable = false;
    private User owner;
    private String request;
}