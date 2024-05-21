package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.RequestService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
public class ItemRequestController {
    private final RequestService requestService;

    /*POST /requests — добавить новый запрос вещи. Основная часть запроса — текст запроса, где пользователь описывает,
    какая именно вещь ему нужна.*/
    @PostMapping
    public ItemRequestDto newItemRequest(@RequestHeader(name = "X-Sharer-User-Id") Long itemId,
                                     @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return null;
    }

    /*GET /requests — получить список своих запросов вместе с данными об ответах на них. Для каждого запроса должны
     указываться описание, дата и время создания и список ответов в формате: id вещи, название, её описание description,
      а также requestId запроса и признак доступности вещи available. Так в дальнейшем, используя указанные id вещей,
       можно будет получить подробную информацию о каждой вещи. Запросы должны возвращаться в отсортированном порядке
       от более новых к более старым.*/

    @GetMapping
    public List<ItemRequestDto> getListRequestsAndAnswers(@RequestHeader(name = "X-Sharer-User-Id") Long requestId) {
        return null;
    }

    /*GET /requests/all?from={from}&size={size} — получить список запросов, созданных другими пользователями. С помощью
    этого эндпоинта пользователи смогут просматривать существующие запросы, на которые они могли бы ответить. Запросы
    сортируются по дате создания: от более новых к более старым. Результаты должны возвращаться постранично. Для этого
    нужно передать два параметра: from — индекс первого элемента, начиная с 0, и size — количество элементов для отображения.*/

    @GetMapping
    public List<ItemRequestDto> getListRequestsOtherUsers(@RequestHeader(name = "X-Sharer-User-Id") Long requestId,
                                                              @RequestParam(defaultValue = "ALL") String state,
                                                              @RequestParam(required = false, defaultValue = "0") int from,
                                                              @RequestParam(required = false, defaultValue = "10") int size) {
        return null;
    }
   /* GET /requests/{requestId} — получить данные об одном конкретном запросе вместе с данными об ответах на него в том же
        формате, что и в эндпоинте GET /requests. Посмотреть данные об отдельном запросе может любой пользователь. */

    @GetMapping(value = "/{requestId}")
    public ItemRequestDto getDataByItemRequest(@RequestHeader(name = "X-Sharer-User-Id") Long requestId,
                                               @PathVariable Long orderId) {
        return null;
    }
}



