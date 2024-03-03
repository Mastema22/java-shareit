package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item save(Item item);
    Item update(Item item);
    Item getById(Long itemId);
    Item delete(Long itemId);
    void deleteItemsByOwner(Long ownerId);
    List<Item> getItemsByOwner(Long ownerId);
    List<Item> getItemsBySearchQuery(String text);
}