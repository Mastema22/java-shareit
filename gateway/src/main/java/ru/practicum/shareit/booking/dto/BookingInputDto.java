package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingInputDto {
    private Long id;
    private Long itemId;
    @FutureOrPresent
    @NonNull
    private LocalDateTime start;
    @Future
    @NonNull
    private LocalDateTime end;
}