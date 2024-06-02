package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Objects;

@Component
public class ItemMapper {

    public ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequestId() != null ? item.getRequestId() : null,
                null,
                null,
                null
        );
    }

    public ItemDto toDto(Item item, Long userId, Booking lastBooking, Booking nextBooking, List<CommentDto> comments) {
        Booking last = Objects.equals(item.getOwnerId(), (userId)) ? lastBooking : null;
        Booking next = Objects.equals(item.getOwnerId(), (userId)) ? nextBooking : null;
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                Objects.nonNull(item.getRequestId()) ? item.getRequestId() : null,
                Objects.nonNull(last) ? new ItemDto.BookingsInItem(last) : null,
                Objects.nonNull(next) ? new ItemDto.BookingsInItem(next) : null,
                comments
        );
    }

    public Item toItem(ItemDto itemDto, Long ownerId) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                ownerId,
                itemDto.getRequestId() != null ? itemDto.getRequestId() : null
        );
    }
}