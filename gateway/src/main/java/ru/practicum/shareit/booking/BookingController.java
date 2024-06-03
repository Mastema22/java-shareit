package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> newBooking(@RequestHeader(name = "X-Sharer-User-Id") Long bookerId,
                                             @Valid @RequestBody BookingDto bookingDto) {
        log.info("Создан новый букинг с ID = " + bookerId);
        return bookingClient.newBooking(bookerId, bookingDto);
    }

    @PatchMapping(value = "/{bookingId}")
    public ResponseEntity<Object> bookingRequest(@RequestHeader(name = "X-Sharer-User-Id") Long ownerId,
                                                 @PathVariable Long bookingId,
                                                 @RequestParam boolean approved) {
        log.info("Букинг с ID = " + bookingId + " подтвержден!");
        return bookingClient.bookingRequest(ownerId, bookingId, approved);
    }

    @GetMapping(value = "/{bookingId}")
    public ResponseEntity<Object> getDataByBooking(@RequestHeader(name = "X-Sharer-User-Id") Long ownerId,
                                                   @PathVariable Long bookingId) {
        log.info("Данный о букинге с ID = " + bookingId + " получены!");
        return bookingClient.getDataByBooking(bookingId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllByBooker(@RequestHeader(name = "X-Sharer-User-Id") Long ownerId,
                                                  @RequestParam(defaultValue = "ALL") String state,
                                                  @RequestParam(required = false, defaultValue = "0") int from,
                                                  @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Получены все бронирования пользователя с ID = " + ownerId);
        return bookingClient.findAllByBooker(ownerId, state, PageRequest.of(from, size));

    }

    @GetMapping(value = "/owner")
    public ResponseEntity<Object> findAllByOwner(@RequestHeader(name = "X-Sharer-User-Id") Long ownerId,
                                                 @RequestParam(defaultValue = "ALL") String state,
                                                 @RequestParam(required = false, defaultValue = "0") @Min(0) int from,
                                                 @RequestParam(required = false, defaultValue = "10") @Min(1) int size) {
        log.info("Получены все бронирования для всех вещей пользователя с ID = " + ownerId);
        return bookingClient.findAllByOwner(ownerId, state, PageRequest.of(from, size));
    }
}