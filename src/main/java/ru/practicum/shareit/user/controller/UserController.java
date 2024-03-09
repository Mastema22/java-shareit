package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.exception.CreateDuplicateEmailException;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        List<UserDto> userList = userService.findAllUser();
        log.info("Список юзеров выведен, их количество \"{}\"", userList.size());
        return userList;
    }

    @PostMapping
    public UserDto saveNewUser(@Valid @RequestBody UserDto userDto) {
        UserDto userPost = userService.addNewUser(userDto);
        log.info("Юзер под номером \"{}\" добавлен", userPost.getId());
        return userPost;
    }

    @GetMapping(value = "/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        UserDto user = userService.findByIdUser(id);
        log.info("Юзер под номером \"{}\" выведен", user.getId());
        return user;
    }

    @PatchMapping(value = "/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody UserDto userDto) throws CreateDuplicateEmailException {
        userService.updateUser(id, userDto);
        log.info("Юзер под номером \"{}\" обновлен", id);
        return userService.findByIdUser(id);
    }

    @DeleteMapping(value = "/{id}")
    public UserDto deleteUser(@PathVariable Long id) {
        UserDto userDto = userService.deleteUser(id);
        log.info("Юзер под номером \"{}\" удален", id);
        return userDto;
    }
}