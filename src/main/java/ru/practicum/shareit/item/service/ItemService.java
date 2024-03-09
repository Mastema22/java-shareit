package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addNewItem(Long itemId, ItemDto itemDto);

    ItemDto updateItem(Long itemId, Long ownerId, ItemDto itemDto);

    ItemDto getItemById(Long itemId);

    public List<ItemDto> getItemsByOwner(Long ownerId);

    ItemDto deleteItem(Long itemId, Long ownerId);

    void deleteItemsByOwner(Long ownerId);

    List<ItemDto> searchItem(String text);
}