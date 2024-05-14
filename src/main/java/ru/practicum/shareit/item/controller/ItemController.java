package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;

    @PostMapping
    public ItemDto saveNewItem(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                               @RequestBody @Valid ItemDto itemDto) {
        UserDto userDto = userService.findByIdUser(userId);
        ItemDto itemPost = itemService.addNewItem(userDto.getId(), itemDto);
        log.info("Вещь под номером \"{}\" добавлена", itemPost.getId());
        return itemPost;
    }

    @PatchMapping(value = "/{itemId}")
    public ItemDto updateItem(@PathVariable Long itemId,
                              @RequestHeader(name = "X-Sharer-User-Id") Long userId,
                              @RequestBody ItemDto itemDto) {
        ItemDto itemUpdate = itemService.updateItem(itemId, userId, itemDto);
        log.info("Вещь под номером \"{}\" обновленна", itemUpdate.getId());
        return itemUpdate;
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        ItemDto itemDto = itemService.getItemById(itemId, userId);
        log.info("Запрос на получение вещи с ID={}", itemId);
        return itemDto;
    }

    @DeleteMapping("/{itemId}")
    public void delete(@PathVariable Long itemId, @RequestHeader(name = "X-Sharer-User-Id") Long ownerId) {
        itemService.deleteItem(itemId, ownerId);
        log.info("Удаление вещи с ID={}", itemId);
    }

    @GetMapping
    public List<ItemDto> getItemsByOwner(@RequestHeader(name = "X-Sharer-User-Id") Long ownerId) {
        List<ItemDto> itemsByOwner = itemService.getItemsByOwner(ownerId);
        log.info("Получение всех вещей владельца с ID={}", ownerId);
        return itemsByOwner;
    }


    @GetMapping("/search")
    public List<ItemDto> getItemsBySearchQuery(@RequestParam String text) {
        List<ItemDto> searchItems = itemService.searchItem(text);
        log.info("Поиск вещи с текстом={}", text);
        return searchItems;
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto create(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                             @PathVariable Long itemId,
                             @RequestBody CommentDto commentDto) {
        return itemService.createComment(userId, itemId, commentDto);
    }

}