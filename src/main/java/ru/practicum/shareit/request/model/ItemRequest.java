package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "Requests")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {
    @Id
    private Long id;
    private String description;
    private User request;
    private Date created;
}
