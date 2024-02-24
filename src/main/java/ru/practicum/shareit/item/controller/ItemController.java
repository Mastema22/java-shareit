package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    @PostMapping
    public Item saveNewItem(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                            @RequestBody @Valid Item item) {
        Item itemPost = itemService.addNewItem(userId, item);
        log.info("Вещь под номером \"{}\" добавлена", itemPost.getId());
        return itemPost;
    }

    @PatchMapping(value = "/{userId}")
    public Item updateItem(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                           @RequestBody @Valid Item item) {
        Item itemUpdate = itemService.updateItem(userId, item);
        log.info("Вещь под номером \"{}\" обновленна", itemUpdate.getId());
        return itemUpdate;
    }
}
