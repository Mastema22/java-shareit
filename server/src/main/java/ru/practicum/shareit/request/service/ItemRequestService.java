package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoByOwner;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemRequestService {

    ItemRequestDto newRequestItem(ItemRequestDto itemRequestDto, Long requesterId, LocalDateTime created);

    ItemRequestDtoByOwner getListRequestsAndAnswers(Long itemRequestId, Long userId);

    List<ItemRequestDto>  getDataByItemRequest(Long requesterId);

    List<ItemRequestDto> getListRequestsOtherUsers(Long userId, Integer from, Integer size);

}