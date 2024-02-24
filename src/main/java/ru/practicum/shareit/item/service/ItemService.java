package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

public interface ItemService {
    Item addNewItem(long it, Item item);
    Item updateItem(long id, Item item);
    Item getItem(long id);
    Item searchItem(String text);
}
