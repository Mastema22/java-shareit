package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoByOwner;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemRequestDto newRequestItem(ItemRequestDto itemRequestDto, Long requesterId, LocalDateTime created) {
        User user = userRepository.findById(requesterId).orElseThrow(
                () -> new UserNotFoundException("Пользователь с ID= " + requesterId + " не найден!"));
        itemRequestDto.setRequester(user);
        itemRequestDto.setCreated(created);
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestDto);
        return itemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public ItemRequestDtoByOwner getListRequestsAndAnswers(Long itemRequestId, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Пользователь с ID= " + userId + " не найден!");
        }
        ItemRequest itemRequest = itemRequestRepository.findById(itemRequestId).orElseThrow(
                () -> new ItemRequestNotFoundException("Запрос ID = " + itemRequestId + " не найден!")
        );
        List<Item> items = itemRepository.findByRequestId(itemRequestId);
        List<ItemDto> reply = new ArrayList<>();
        for (Item item : items) {
            reply.add(itemMapper.toItemDto(item));
        }
        return ItemRequestMapper.doItemRequestDtoByOwner(itemRequest, reply);
    }

    @Override
    public List<ItemRequestDto> getDataByItemRequest(Long requesterId) {
        if (!userRepository.existsById(requesterId)) {
            throw new UserNotFoundException("Пользователь с ID = " + requesterId + " не найден!");
        }
        ItemRequest itemRequest = itemRequestRepository.findById(requesterId).orElseThrow(
                () -> new ItemRequestNotFoundException("Запрос ID = " + requesterId + " не найден!")
        );
        List<ItemRequest> itemRequestList = itemRequestRepository.findAllByRequesterIdNotOrderByCreatedDesc(requesterId);
        if (itemRequestList.isEmpty()) {
            return  Collections.emptyList();
        }
        

        return itemRequestRepository.findAllByRequesterId(requesterId, Sort.by(Sort.Direction.DESC, "created")).stream()
                .map(itemRequestMapper::toItemRequestDto)
                .collect(toList());
    }

    @Override
    public List<ItemRequestDto> getListRequestsOtherUsers(Long userId, Integer from, Integer size) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Пользователь с ID= " + userId + " не найден!");
        }
        List<ItemRequestDto> listItemRequestDto = new ArrayList<>();
        Pageable pageable;
        Page<ItemRequest> page;
        Pagination pager = new Pagination(from, size);
        Sort sort = Sort.by(Sort.Direction.DESC, "created");
        if (size == null) {
            List<ItemRequest> listItemRequest = itemRequestRepository.findAllByRequesterIdNotOrderByCreatedDesc(userId);
            listItemRequestDto
                    .addAll(listItemRequest.stream().skip(from).map(itemRequestMapper::toItemRequestDto).collect(toList()));
        } else {
            for (int i = pager.getIndex(); i < pager.getTotalPages(); i++) {
                pageable = PageRequest.of(i, pager.getPageSize(), sort);
                page = itemRequestRepository.findAllByRequesterIdNot(userId, pageable);
                listItemRequestDto.addAll(page.stream().map(itemRequestMapper::toItemRequestDto).collect(toList()));
                if (!page.hasNext()) {
                    break;
                }
            }
            listItemRequestDto = listItemRequestDto.stream().limit(size).collect(toList());
        }
        return listItemRequestDto;

    }
}
