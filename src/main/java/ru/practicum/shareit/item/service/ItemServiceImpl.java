package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final ItemMapper mapper;

    public ItemServiceImpl(@Qualifier("ItemRepositoryImpl") ItemRepository repository, ItemMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ItemDto addNewItem(Long itemId, ItemDto itemDto) {
        return mapper.toItemDto(repository.save(mapper.toItem(itemDto, itemId)));
    }

    @Override
    public ItemDto updateItem(Long itemId, Long ownerId, ItemDto itemDto) {
        if (itemDto.getId() == null) {
            itemDto.setId(itemId);
        }
        Item oldItem = repository.getById(itemId);
        if (!oldItem.getOwnerId().equals(ownerId)) {
            throw new ItemNotFoundException("У пользователя нет такой вещи!");
        }
        return mapper.toItemDto(repository.update(mapper.toItem(itemDto, ownerId)));
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        return mapper.toItemDto(repository.getById(itemId));
    }

    @Override
    public List<ItemDto> getItemsByOwner(Long ownerId) {
        return repository.getItemsByOwner(ownerId).stream()
                .map(mapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto deleteItem(Long itemId, Long ownerId) {
        Item item = repository.getById(itemId);
        if (!item.getOwnerId().equals(ownerId)) {
            throw new ItemNotFoundException("У пользователя нет такой вещи!");
        }
        return mapper.toItemDto(repository.delete(itemId));
    }

    @Override
    public void deleteItemsByOwner(Long ownerId) {
        repository.deleteItemsByOwner(ownerId);
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        if (text.isEmpty()) {
            text = "0";
        }
        text = text.toLowerCase();
        return repository.getItemsBySearchQuery(text).stream()
                .map(mapper::toItemDto)
                .collect(Collectors.toList());
    }
}