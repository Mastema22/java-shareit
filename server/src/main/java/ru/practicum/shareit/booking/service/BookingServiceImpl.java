package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.model.Status.REJECTED;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingOutputDto newBooking(Long bookerId, BookingInputDto inputDto) {
        Booking booking = bookingMapper.toBooking(inputDto);
        Item item = itemRepository.findById(booking.getItem().getId())
                .orElseThrow(() -> new ItemNotFoundException("Вещь с ID= " + booking.getItem().getId() + " не найдена!"));
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID=" + bookerId + " не найден!"));
        if (!item.getAvailable()) {
            throw new ItemNotAvailableException("Вещь уже забронирована!");
        }
        if (booking.getEnd().isBefore(booking.getStart()) || booking.getStart().equals(booking.getEnd())) {
            throw new IncorrectTimeException("Задано неверное время!");
        }
        if (item.getOwnerId().equals(bookerId)) {
            throw new SelfRentOwnerException("Пользовательй не может арендовать свою вещь!");
        }
        Booking newBooking = new Booking(null, booking.getStart(), booking.getEnd(), item, booker, booking.getStatus());
        return bookingMapper.toBookingDto(bookingRepository.save(newBooking));
    }
    @Transactional
    @Override
    public BookingOutputDto bookingRequest(Long ownerId, Long bookingId, boolean approved) {
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new UserNotFoundException("Пользователь с ID=" + ownerId + " не найден!"));
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundBookingException("Вещь с ID= " + bookingId + " не найдена!"));
        Item item = itemRepository.findById(booking.getItem().getId())
                .orElseThrow(() -> new ItemNotFoundException("Вещь с ID= " + booking.getItem().getId() + " не найдена!"));
        if (item.getOwnerId().equals(owner.getId())) {
            if (booking.getStatus().equals(Status.APPROVED) || booking.getStatus().equals(REJECTED)) {
                throw new ValidationException("У брони уже есть статус APPROVED/REJECTED !");
            }
            if (approved) {
                booking.setStatus(Status.APPROVED);
            } else {
                booking.setStatus(REJECTED);
            }
            return bookingMapper.toBookingDto(bookingRepository.save(booking));
        } else {
            throw new DifferentUserAndOwnerException("У пользователя нет права менять статус брони!");
        }
    }

    @Override
    public BookingOutputDto getDataByBooking(Long bookerId, Long bookingId) {
        User owner = userRepository.findById(bookerId).orElseThrow(() -> new UserNotFoundException("Пользователь с ID=" + bookerId + " не найден!"));
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundBookingException("Вещь с ID= " + bookingId + " не найдена!"));
        Item item = itemRepository.findById(booking.getItem().getId())
                .orElseThrow(() -> new ItemNotFoundException("Вещь с ID= " + booking.getItem().getId() + " не найдена!"));
        if (item.getOwnerId().equals(owner.getId()) || booking.getBooker().getId().equals(owner.getId())) {
            return bookingMapper.toBookingDto(booking);
        } else {
            throw new DifferentUserAndOwnerException("У пользователя нет права просмотра информации о бронировани!");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingOutputDto> findAllByBooker(Long bookerId, String state, Pageable pageRequest) {
        List<BookingOutputDto> bookingsDto = getBookingsBooker(state, bookerId);
        if (bookingsDto.isEmpty()) {
            throw new NotFoundBookingException("Бронирования пользователя с ID = " + bookerId + " не найдены!");
        }
        return bookingsDto.subList(pageRequest.getPageNumber(),
                Math.min(bookingsDto.size(), pageRequest.getPageNumber() + pageRequest.getPageSize()));
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingOutputDto> findAllByOwner(Long ownerId, String state, Pageable pageRequest) {
        List<BookingOutputDto> bookingsDto = getBookingsOwner(state, ownerId);
        if (bookingsDto.isEmpty()) {
            throw new NotFoundBookingException("Нет бронирований у пользоватяеля с ID = " + ownerId);
        }
        return bookingsDto.subList(pageRequest.getPageNumber(),
                Math.min(bookingsDto.size(), pageRequest.getPageNumber() + pageRequest.getPageSize()));
    }

    private List<BookingOutputDto> getBookingsBooker(String state, Long bookerId) {
        User booker = userRepository.findById(bookerId).orElseThrow(() -> new UserNotFoundException("Пользователь с ID=" + bookerId + " не найден!"));
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        List<Booking> bookings;
        switch (State.convert(state)) {
            case WAITING:
                bookings = bookingRepository.findAllByBookerIdAndStatus(booker.getId(), Status.WAITING, sort);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByBookerIdAndStatus(booker.getId(),
                        REJECTED, sort);
                break;
            case PAST:
                bookings = bookingRepository.findAllByBookerIdAndEndBefore(booker.getId(),
                        LocalDateTime.now(), sort);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByBookerIdAndStartAfter(booker.getId(),
                        LocalDateTime.now(), sort);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(booker.getId(),
                        LocalDateTime.now(), sort);
                break;
            default:
                bookings = bookingRepository.findAllByBookerId(booker.getId(), sort);
        }
        return bookings
                .stream()
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    private List<BookingOutputDto> getBookingsOwner(String state, Long bookerId) {
        User booker = userRepository.findById(bookerId).orElseThrow(() -> new UserNotFoundException("Пользователь с ID=" + bookerId + " не найден!"));
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        List<Booking> bookings;
        switch (State.convert(state)) {
            case WAITING:
                bookings = bookingRepository.findAllByOwnerIdAndStatus(booker.getId(), Status.WAITING, sort);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByOwnerIdAndStatus(booker.getId(),
                        REJECTED, sort);
                break;
            case PAST:
                bookings = bookingRepository.findAllByOwnerIdAndEndBefore(booker.getId(),
                        LocalDateTime.now(), sort);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByOwnerIdAndStartAfter(booker.getId(),
                        LocalDateTime.now(), sort);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByOwnerIdAndStartBeforeAndEndAfter(booker.getId(),
                        LocalDateTime.now(), sort);
                break;
            default:
                bookings = bookingRepository.findAllByOwnerId(booker.getId(), sort);
        }
        return bookings
                .stream()
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}