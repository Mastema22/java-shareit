package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;

import java.util.List;

public interface BookingService {
    BookingOutputDto newBooking(Long bookerId, BookingDto inputDto);

    BookingOutputDto bookingRequest(Long ownerId, Long bookingId, boolean approved);

    BookingOutputDto getDataByBooking(Long bookerId, Long bookingId);

    List<BookingOutputDto> findAllByBooker(Long ownerId, String state, PageRequest pageRequest);

    List<BookingOutputDto> findAllByOwner(Long ownerId, String state, PageRequest pageRequest);
}
