package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoByOwner;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private final String userIdHeader = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequestDto newItemRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                         @RequestHeader(name = userIdHeader) Long requesterId) {
        log.info("Получен POST-запрос к эндпоинту: '/requests' " +
                "на создание запроса вещи от пользователя с ID = {}", requesterId);
        return itemRequestService.newRequestItem(itemRequestDto, requesterId, LocalDateTime.now());


    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoByOwner getListRequestsAndAnswers(@PathVariable("requestId") Long itemRequestId,
                                                                 @RequestHeader(name = userIdHeader) Long userId) {
        log.info("Получен GET-запрос к эндпоинту: '/requests' на получение запроса с ID = {}", itemRequestId);
        return itemRequestService.getListRequestsAndAnswers(itemRequestId, userId);

    }

    @GetMapping
    public List<ItemRequestDto>  getDataByItemRequest(@RequestHeader(name = userIdHeader) Long userId) {
        log.info("Получен GET-запрос к эндпоинту: '/requests' на получение запросов пользователя ID = {}",
                userId);
        return itemRequestService.getDataByItemRequest(userId);

    }

    @GetMapping("/all")
    public List<ItemRequestDto> getListRequestsOtherUsers(@RequestHeader(name = userIdHeader) Long userId,
                                                          @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                          @RequestParam(required = false) @Min(1) Integer size) {
        log.info("Получен GET-запрос к эндпоинту: '/requests/all' от пользователя с ID = {} на получение всех запросов",
                userId);
        return itemRequestService.getListRequestsOtherUsers(userId, from, size);
    }
}