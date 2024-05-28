package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ItemRequestDto {
    private Long id;
    @NotBlank(message = "поле не должно быть пустым")
    @Size(max = 512, message = "Превышена максимальная длина сообщения")
    private String description;
    private User requester;
    private LocalDateTime created;

}