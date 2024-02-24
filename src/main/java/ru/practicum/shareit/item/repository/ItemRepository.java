package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

public interface ItemRepository {
    Item saveItem(long it, Item item);
    Item updateItem(long id, Item item);
    Item getItemById(long id);
    Item searchItem(String text);
}
