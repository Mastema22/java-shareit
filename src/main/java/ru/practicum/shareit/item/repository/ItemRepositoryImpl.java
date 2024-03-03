package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository("ItemRepositoryImpl")
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    private final List<Item> itemList = new ArrayList<>();
    private Long id = 0L;

    @Override
    public Item save(Item item) {
        if (item.getName().isEmpty() || item.getDescription().isEmpty() || item.getAvailable() == null) {
            throw new ValidationException("У вещи некорректные данные");
        }
        item.setId(++id);
        itemList.add(item);
        return item;
    }

    @Override
    public Item update(Item item) {
        Item existingItem = itemList.stream()
                .filter(i -> i.getId().equals(item.getId()))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("Вещь с ID=" + item.getId() + " не найден!"));
        if (item.getName() != null) existingItem.setName(item.getName());
        if (item.getDescription() != null) existingItem.setDescription(item.getDescription());
        if (item.getAvailable() != null) existingItem.setAvailable(item.getAvailable());
        return existingItem;
    }

    @Override
    public Item getById(Long itemId) {
        if (itemId == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        return itemList.stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ItemNotFoundException("Вещь с ID= " + itemId + " не найдена!"));
    }

    @Override
    public Item delete(Long itemId) {
        if (itemId == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        Item item = getById(itemId);
        itemList.remove(item);
        return item;
    }

    @Override
    public void deleteItemsByOwner(Long ownerId) {
        List<Item> deleteItems = itemList.stream()
                .filter(item -> item.getOwnerId().equals(ownerId))
                .collect(Collectors.toList());
        for (Item deleteId : deleteItems) {
            itemList.remove(deleteId);
        }
    }

    @Override
    public List<Item> getItemsByOwner(Long ownerId) {
        return itemList.stream()
                .filter(item -> item.getOwnerId().equals(ownerId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> getItemsBySearchQuery(String text) {
        List<Item> searchItems = new ArrayList<>();
        if (!text.isBlank()) {
            searchItems = itemList.stream()
                    .filter(Item::getAvailable)
                    .filter(item -> item.getName().toLowerCase().contains(text) ||
                            item.getDescription().toLowerCase().contains(text))
                    .collect(Collectors.toList());
        } else {
            throw new ValidationException("Передан пустой аргумент!");
        }
        return searchItems;
    }
}