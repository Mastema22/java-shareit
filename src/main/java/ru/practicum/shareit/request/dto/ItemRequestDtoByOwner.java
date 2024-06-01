package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDtoByOwner {
    private Long id;
    @NotBlank(message = "поле не должно быть пустым")
    @Size(max = 512, message = "Превышена максимальная длина сообщения")
    private String description;
    private User requester;
    private LocalDateTime created;
    private List<ItemDto> items;
}

