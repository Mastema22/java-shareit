package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> saveNewItem(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                              @RequestBody @Valid ItemDto itemDto) {
        log.info("Вещь под номером \"{}\" добавлена", itemDto.getId());
        return itemClient.addNewItem(userId, itemDto);
    }

    @ResponseBody
    @PatchMapping(value = "/{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable Long itemId,
                                             @RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                             @RequestBody ItemDto itemDto) {
        log.info("Вещь под номером \"{}\" обновленна", itemId);
        return itemClient.updateItem(itemId, userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос на получение вещи с ID={}", itemId);
        return itemClient.getItemById(userId, itemId);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> delete(@PathVariable Long itemId, @RequestHeader(name = "X-Sharer-User-Id") Long ownerId) {
        log.info("Удаление вещи с ID={}", itemId);
        return itemClient.deleteItem(itemId, ownerId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsByOwner(@RequestHeader(name = "X-Sharer-User-Id") Long ownerId) {
        log.info("Получение всех вещей владельца с ID={}", ownerId);
        return itemClient.getItemsByOwner(ownerId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemsBySearchQuery(@RequestParam String text) {
        log.info("Поиск вещи с текстом={}", text);
        return itemClient.searchItem(text);
    }

    @ResponseBody
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> create(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                         @PathVariable Long itemId,
                                         @RequestBody CommentDto commentDto) {
        return itemClient.createComment(commentDto, itemId, userId);
    }

}