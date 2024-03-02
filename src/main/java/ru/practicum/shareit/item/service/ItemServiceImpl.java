package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

@Service
public class ItemServiceImpl implements ItemService {
    private ItemRepository repository;
    @Override
    public Item addNewItem(long it, Item item) {
        return repository.saveItem(it, item);
    }
    @Override
    public Item updateItem(long id, Item item) {
        return repository.updateItem(id, item);
    }
    @Override
    public Item getItem(long id) {
        return repository.getItemById(id);
    }
    @Override
    public Item searchItem(String text) {
        return repository.searchItem(text);
    }
}

