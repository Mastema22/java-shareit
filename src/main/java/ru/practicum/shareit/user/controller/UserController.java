package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.exception.CreateDuplicateEmailException;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        List<User> userList = userService.findAllUser();
        log.info("Список юзеров выведен, их количество \"{}\"", userList.size());
        return userList;
    }

    @PostMapping
    public User saveNewUser(@Valid @RequestBody User user) {
        User userPost = userService.addNewUser(user);
        log.info("Юзер под номером \"{}\" добавлен", user.getId());
        return userPost;
    }

    @GetMapping(value = "/{id}")
    public User getUserById(@PathVariable long id) {
        User user = userService.findByIdUser(id);
        log.info("Юзер под номером \"{}\" выведен", user.getId());
        return user;
    }

    @PatchMapping(value = "/{id}")
    public User updateUser(@PathVariable long id, @RequestBody User user) throws CreateDuplicateEmailException {
        userService.updateUser(id, user);
        log.info("Юзер под номером \"{}\" обновлен", id);
        return userService.findByIdUser(id);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        log.info("Юзер под номером \"{}\" удален", id);
    }
}
