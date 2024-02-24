package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    private final UserRepository userRepository;
    private final List<Item> itemList = new ArrayList<>();

    private long itemId = 0;

    @Override
    public Item saveItem(long id, Item item) {
        User user = userRepository.findById(id);
        item.setId(++itemId);
        item.setOwner(user);
        itemList.add(item);
        return item;
    }

    @Override
    public Item updateItem(long id, Item item) {
        User user = userRepository.findById(id);
        Item itemUpdate = new Item();
        for (Item item1 : itemList) {
            if (item1.getId().equals(item.getId())) {
                item1.setName(item.getName());
                item1.setDescription(item.getDescription());
                item1.setOwner(item.getOwner());
                item1.setAvailable(item.isAvailable());
                item1.setRequest(item.getRequest());
            }
            itemUpdate = item1;
        }
        return itemUpdate;
    }

    @Override
    public Item getItemById(long id) {
        return itemList.stream().filter(item -> item.getId().equals(id)).findFirst().orElseThrow();
    }

    @Override
    public Item searchItem(String text) {
        return itemList.get(1);
    }

}
