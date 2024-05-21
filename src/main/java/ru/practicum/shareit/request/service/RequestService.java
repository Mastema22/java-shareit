package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface RequestService {

    ItemRequestDto newRequestItem(String description);

    List<ItemRequestDto> getListRequestsAndAnswers(Long id);

    List<ItemRequestDto> getListRequestsOtherUsers();

    ItemRequestDto getDataByItemRequest(Long id);


}
