package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private static final String USER_ID = "X-Sharer-User-Id";
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> newBooking(@RequestHeader(USER_ID) @Positive Long bookerId,
                                             @Valid @RequestBody BookingDto bookingDto) {
        ResponseEntity<Object> newBooking = bookingClient.newBooking(bookerId, bookingDto);
        log.info("Создан новый букинг с ID = " + bookerId);
        return newBooking;
    }

    @PatchMapping(value = "/{bookingId}")
    public ResponseEntity<Object> bookingRequest(@RequestHeader(USER_ID) @Positive Long ownerId,
                                                 @PathVariable @Positive Long bookingId,
                                                 @RequestParam Boolean approved) {
        ResponseEntity<Object> bookingRequest = bookingClient.bookingRequest(ownerId, bookingId, approved);
        log.info("Букинг с ID = " + bookingId + " подтвержден!");
        return bookingRequest;
    }

    @GetMapping(value = "/{bookingId}")
    public ResponseEntity<Object> getDataByBooking(@RequestHeader(USER_ID) @Positive Long ownerId,
                                                   @PathVariable @Positive Long bookingId) {
        ResponseEntity<Object> bookings = bookingClient.getDataByBooking(ownerId, bookingId);
        log.info("Данный о букинге с ID = " + bookingId + " получены!");
        return bookings;
    }

    @GetMapping
    public ResponseEntity<Object> findAllByBooker(@RequestHeader(USER_ID) @Positive Long ownerId,
                                                  @RequestParam(name = "state", defaultValue = "ALL") String state,
                                                  @RequestParam(required = false, defaultValue = "0")
                                                  @Min(0) int from,
                                                  @RequestParam(required = false, defaultValue = "10")
                                                  @Min(1) int size) {
        BookingState statusFilter = BookingState.from(state)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
        ResponseEntity<Object> bookings = bookingClient.findAllByBooker(ownerId, statusFilter, from, size);
        log.info("Получены все бронирования пользователя с ID = " + ownerId);
        return bookings;

    }

    @GetMapping(value = "/owner")
    public ResponseEntity<Object> findAllByOwner(@RequestHeader(USER_ID) @Positive Long ownerId,
                                                 @RequestParam(defaultValue = "ALL") String state,
                                                 @RequestParam(required = false, defaultValue = "0")
                                                 @Min(0) int from,
                                                 @RequestParam(required = false, defaultValue = "10")
                                                 @Min(1) int size) {
        log.info("Получены все бронирования для всех вещей пользователя с ID = " + ownerId);
        BookingState statusFilter = BookingState.from(state)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
        ResponseEntity<Object> bookings = bookingClient.findAllByOwner(ownerId, statusFilter, from, size);
        return bookings;
    }
}