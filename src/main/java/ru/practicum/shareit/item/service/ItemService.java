package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addNewItem(Long itemId, ItemDto itemDto);

    ItemDto updateItem(Long itemId, Long ownerId, ItemDto itemDto);

    ItemDto getItemById(Long itemId, Long userId);

    List<ItemDto> getItemsByOwner(Long ownerId);

    void deleteItem(Long itemId, Long ownerId);

    void deleteItemsByOwner(Long ownerId);

    List<ItemDto> searchItem(String text);

    CommentDto createComment(Long userId, Long itemId, CommentDto commentDto);
}