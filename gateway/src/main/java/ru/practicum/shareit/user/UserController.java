package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Список юзеров выведен");
        return userClient.findAllUser();
    }

    @PostMapping
    public ResponseEntity<Object> saveNewUser(@Valid @RequestBody UserDto userDto) {
        log.info("Юзер под номером \"{}\" добавлен", userDto.getId());
        return userClient.addNewUser(userDto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable @Positive Long id) {
        log.info("Юзер под номером \"{}\" выведен", id);
        return userClient.findByIdUser(id);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable @Positive Long id, @RequestBody UserDto userDto) {
        log.info("Юзер под номером \"{}\" обновлен", id);
        return userClient.updateUser(id,userDto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable @Positive Long id) {
        log.info("Юзер под номером \"{}\" удален", id);
        return userClient.deleteUser(id);
    }
}