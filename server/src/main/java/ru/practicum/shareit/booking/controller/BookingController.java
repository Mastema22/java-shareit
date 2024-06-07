package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {
    private static final String USER_ID = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @PostMapping
    public BookingOutputDto newBooking(@RequestHeader(USER_ID) Long bookerId,
                                       @RequestBody BookingInputDto bookingDto) {
        BookingOutputDto bookingOutputDto = bookingService.newBooking(bookerId, bookingDto);
        log.info("Создан новый букинг с ID = " + bookerId);
        return bookingOutputDto;
    }

    @PatchMapping(value = "/{bookingId}")
    public BookingOutputDto bookingRequest(@RequestHeader(USER_ID) Long ownerId,
                                           @PathVariable Long bookingId,
                                           @RequestParam boolean approved) {
        BookingOutputDto bookingOutputDto = bookingService.bookingRequest(ownerId, bookingId, approved);
        log.info("Букинг с ID = " + bookingId + " подтвержден!");
        return bookingOutputDto;
    }

    @GetMapping(value = "/{bookingId}")
    public BookingOutputDto getDataByBooking(@RequestHeader(USER_ID) Long ownerId,
                                             @PathVariable Long bookingId) {
        BookingOutputDto bookingOutputDto = bookingService.getDataByBooking(ownerId, bookingId);
        log.info("Данный о букинге с ID = " + bookingId + " получены!");
        return bookingOutputDto;
    }

    @GetMapping
    public List<BookingOutputDto> findAllByBooker(@RequestHeader(USER_ID) Long ownerId,
                                                  @RequestParam String state,
                                                  @RequestParam int from,
                                                  @RequestParam int size) {
        Pageable pageable = PageRequest.of(from, size);
        List<BookingOutputDto> bookingOutputDtoList = bookingService.findAllByBooker(ownerId, state, pageable);
        log.info("Получены все бронирования пользователя с ID = " + ownerId);
        return bookingOutputDtoList;

    }

    @GetMapping(value = "/owner")
    public List<BookingOutputDto> findAllByOwner(@RequestHeader(USER_ID) Long ownerId,
                                                 @RequestParam String state,
                                                 @RequestParam int from,
                                                 @RequestParam int size) {
        Pageable pageable = PageRequest.of(from, size);
        List<BookingOutputDto> bookingOutputDtoList = bookingService.findAllByOwner(ownerId, state, pageable);
        log.info("Получены все бронирования для всех вещей пользователя с ID = " + ownerId);
        return bookingOutputDtoList;
    }
}