package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDto {
    private Long id;
    private Long itemId;
    @FutureOrPresent
    @NonNull
    private LocalDateTime start;
    @Future
    @NonNull
    private LocalDateTime end;
    private ItemDto item;
    private UserDto booker;
    private BookingStatus status;

}