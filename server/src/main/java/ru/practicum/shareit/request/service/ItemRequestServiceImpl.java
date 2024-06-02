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
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
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
    private final UserMapper userMapper;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemRequestDto newRequestItem(ItemRequestDto itemRequestDto, Long requesterId, LocalDateTime created) {
        User user = userRepository.findById(requesterId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID = " + requesterId + " не найден!"));
        UserDto userDto = userMapper.toUserDto(user);
        itemRequestDto.setRequester(userDto);
        itemRequestDto.setCreated(created);
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestDto);
        return itemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public ItemRequestDtoByOwner getListRequestsAndAnswers(Long itemRequestId, Long userId) {
        checkExistUser(userId);
        ItemRequest itemRequest = itemRequestRepository.findById(itemRequestId).orElseThrow(
                () -> new ItemRequestNotFoundException("Запрос ID = " + itemRequestId + " не найден!")
        );
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequesterIdNotOrderByCreatedDesc(userId);
        List<ItemDto> reply = itemRepository.findByRequestId(itemRequestId)
                .stream()
                .map(itemMapper::toItemDto)
                .collect(toList());
        return ItemRequestMapper.doItemRequestDtoByOwner(itemRequest, reply);
    }

    @Override
    public List<ItemRequestDto> getDataByItemRequest(Long requesterId) {
        checkExistUser(requesterId);
        List<ItemRequestDto> listItemRequestDto = itemRequestRepository.findAllByRequesterId(requesterId, Sort.by(Sort.Direction.DESC, "created")).stream()
                .map(itemRequestMapper::toItemRequestDto)
                .collect(toList());
        return checkItemRequestDto(listItemRequestDto);
    }

    @Override
    public List<ItemRequestDto> getListRequestsOtherUsers(Long userId, Integer from, Integer size) {
        checkExistUser(userId);
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
        return checkItemRequestDto(listItemRequestDto);
    }

    private void checkExistUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Пользователь с ID = " + userId + " не найден!");
        }
    }

    private List<ItemRequestDto> checkItemRequestDto(List<ItemRequestDto> listItemRequestDto) {
        List<Long> requestIds = listItemRequestDto.stream()
                .map(ItemRequestDto::getId)
                .collect(toList());
        List<Item> items = itemRepository.findByRequestIdIn(requestIds);
        for (ItemRequestDto itemRequestDto : listItemRequestDto) {
            List<Item> requestItems = items.stream()
                    .filter(item -> item.getRequestId().equals(itemRequestDto.getId()))
                    .collect(toList());
            if (!requestItems.isEmpty()) {
                List<ItemDto> itemDtos = requestItems.stream()
                        .map(itemMapper::toItemDto)
                        .collect(toList());
                itemRequestDto.setItems(itemDtos);
            } else {
                itemRequestDto.setItems(Collections.emptyList());
            }
        }
        return listItemRequestDto;
    }
}
