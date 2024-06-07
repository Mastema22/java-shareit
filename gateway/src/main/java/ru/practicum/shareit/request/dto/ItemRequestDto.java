package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemRequestDto {
    private Long id;
    @NotBlank(message = "поле не должно быть пустым")
    @Size(max = 512, message = "Превышена максимальная длина сообщения")
    private String description;
    private UserDto requester;
    private LocalDateTime created;
    private List<ItemDto> items;

}