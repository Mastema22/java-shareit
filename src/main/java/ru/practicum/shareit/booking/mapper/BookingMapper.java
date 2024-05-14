package ru.practicum.shareit.booking.mapper;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

@Component
@Data
public class BookingMapper {
    private final ItemRepository repository;

    public BookingOutputDto toBookingDto(Booking booking) {
        return new BookingOutputDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem(),
                booking.getBooker(),
                booking.getStatus()
        );
    }

    public Booking toBooking(BookingInputDto dto) {
        Item item = repository.findById(dto.getItemId()).orElseThrow(() -> new ItemNotFoundException("Вещб с ID = " + dto.getItemId() + "не найдена!"));
        return new Booking(
                null,
                dto.getStart(),
                dto.getEnd(),
                item,
                null,
                Status.WAITING
        );
    }
}