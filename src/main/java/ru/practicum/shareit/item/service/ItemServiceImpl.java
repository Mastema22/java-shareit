package ru.practicum.shareit.item.service;

import lombok.Data;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Data
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;


    @Override
    public ItemDto addNewItem(Long itemId, ItemDto itemDto) {
        if (itemDto.getName().isEmpty() || itemDto.getDescription().isEmpty() || itemDto.getAvailable() == null) {
            throw new ValidationException("У вещи некорректные данные");
        }
        return itemMapper.toItemDto(itemRepository.save(itemMapper.toItem(itemDto, itemId)));
    }

    @Override
    public ItemDto updateItem(Long itemId, Long ownerId, ItemDto itemDto) {
        Item existingItem = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Вещь с ID=" + itemId + " не найдена!"));
        if (!existingItem.getOwnerId().equals(ownerId))
            throw new ItemNotFoundException("У пользователя нет такой вещи!");
        if (itemDto.getName() != null) existingItem.setName(itemDto.getName());
        if (itemDto.getDescription() != null) existingItem.setDescription(itemDto.getDescription());
        if (itemDto.getAvailable() != null) existingItem.setAvailable(itemDto.getAvailable());
        return itemMapper.toItemDto(itemRepository.save(existingItem));
    }

    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Вещь с ID=" + itemId + " не найдена!"));
        ItemDto itemDto = itemMapper.toDto(
                item, item.getOwnerId(),
                null,
                null,
                getCommentsDto(itemId));
        if (item.getOwnerId().equals(userId)) {
            itemDto = itemMapper.toDto(
                    item, item.getOwnerId(),
                    getLastBooking(itemId),
                    getNextBooking(itemId, getLastBooking(itemId)),
                    getCommentsDto(itemId));
        }
        return itemDto;
    }

    @Override
    public List<ItemDto> getItemsByOwner(Long ownerId) {
        List<Item> itemList = itemRepository.getItemsByOwner(ownerId);
        List<ItemDto> itemDtoList = new ArrayList<>();
        itemList.forEach(item -> itemDtoList.add(itemMapper.toDto(
                item, item.getOwnerId(),
                getLastBooking(item.getId()),
                getNextBooking(item.getId(), getLastBooking(item.getId())),
                getCommentsDto(item.getId()))));
        return itemDtoList;
    }

    @Override
    public void deleteItem(Long itemId, Long ownerId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Вещь с ID=" + itemId + " не найдена!"));
        if (!item.getOwnerId().equals(ownerId)) throw new ItemNotFoundException("У пользователя нет такой вещи!");

        itemRepository.deleteItem(itemId, ownerId);
    }

    @Override
    public void deleteItemsByOwner(Long ownerId) {
        itemRepository.deleteItemsByOwner(ownerId);
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        if (text.isEmpty()) {
            text = "0";
        }
        text = text.toLowerCase();
        return itemRepository.getItemsBySearchQuery(text).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto createComment(Long userId, Long itemId, CommentDto commentDto) {
        User user = userRepository.findUserById(userId);
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Вещь с ID=" + itemId + " не найдена!"));
       if (!commentDto.getAuthorName().equals(user.getName())) {
            throw new ValidationException("Некорркетный статус!");
        }
        Booking booking = bookingRepository
                .findFirstByItemIdAndEndBeforeAndStatus(itemId, LocalDateTime.now(), Status.APPROVED)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID = " + userId + " не арендовал вещь с ID = " + itemId));
        if (booking.getBooker().getId().equals(userId)
                && Objects.equals(Booking.getState(booking), State.PAST)
                && !Objects.equals(booking.getStatus(), (Status.CANCELLED))
                && !Objects.equals(booking.getStatus(), (Status.REJECTED))) {
            return commentMapper.toDto(commentRepository.save(commentMapper.toNewEntity(commentDto,
                    item, user)));
        } else
            throw new UserNotFoundException("Некорркетный статус!");
    }

    private Booking getNextBooking(Long itemId, Booking lastBooking) {
        LocalDateTime lastBookingEnd;
        if (Objects.isNull(lastBooking)) {
            lastBookingEnd = LocalDateTime.now();
        } else {
            lastBookingEnd = lastBooking.getEnd();
        }
        return bookingRepository.findFirstByItemIdAndStatusAndStartAfterOrderByStart(itemId,
                Status.APPROVED, lastBookingEnd).orElse(null);
    }

    private Booking getLastBooking(Long itemId) {
        return bookingRepository.findFirstByItemIdAndStatusAndStartBeforeOrderByEndDesc(itemId,
                Status.APPROVED, LocalDateTime.now()).orElse(null);
    }

    private List<CommentDto> getCommentsDto(Long itemId) {
        return commentRepository.findAllByItemId(itemId, null).stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }
}