package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/requests")
@Validated
public class ItemRequestController {
    private final String USER_ID = "X-Sharer-User-Id";
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> newItemRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                                 @RequestHeader(USER_ID) @Positive Long requesterId) {
        log.info("Получен POST-запрос к эндпоинту: '/requests' " +
                "на создание запроса вещи от пользователя с ID = {}", requesterId);
        return requestClient.newRequestItem(itemRequestDto, requesterId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getListRequestsAndAnswers(@PathVariable("requestId") Long itemRequestId,
                                                            @RequestHeader(USER_ID) @Positive Long userId) {
        log.info("Получен GET-запрос к эндпоинту: '/requests' на получение запроса с ID = {}", itemRequestId);
        return requestClient.getListRequestsAndAnswers(itemRequestId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getDataByItemRequest(@RequestHeader(USER_ID) @Positive Long userId) {
        log.info("Получен GET-запрос к эндпоинту: '/requests' на получение запросов пользователя ID = {}", userId);
        return requestClient.getDataByItemRequest(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getListRequestsOtherUsers(@RequestHeader(USER_ID) @Positive Long userId,
                                                            @PositiveOrZero @RequestParam(defaultValue = "0")
                                                            @Min(0) Integer from,
                                                            @RequestParam(required = false, defaultValue = "10")
                                                            @Min(1) Integer size) {
        log.info("Получен GET-запрос к эндпоинту: '/requests/all' от пользователя с ID = {} на получение всех запросов",
                userId);
        return requestClient.getListRequestsOtherUsers(userId, from, size);
    }
}