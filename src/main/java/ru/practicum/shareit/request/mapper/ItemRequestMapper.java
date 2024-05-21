package ru.practicum.shareit.request.mapper;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;

@Component
@Data
public class ItemRequestMapper {

    private final ItemRequestRepository repository;

    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequest(),
                itemRequest.getCreated()
        );
    }

    public ItemRequest toItemRequest(ItemRequestDto dto) {
        return new ItemRequest(
                null,
                dto.getDescription(),
                dto.getRequest(),
                dto.getCreated()
        );
    }
}
