package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.CreateDuplicateEmailException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.UserAlreadyExsistExeption;
import ru.practicum.shareit.user.model.User;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> userList = new HashMap<>();
    private Long id = 0L;

    @Override
    public List<User> findAll() {
        return (List<User>) userList.values();
    }

    @Override
    public User findById(Long userId) {
        if (!userList.containsKey(userId)) {
            throw new EntityNotFoundException("Пользователя с ID=" + userId + " нет в текущем списке!");
        }
        return userList.values()
                .stream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst()
                .orElseThrow();
    }

    @Override
    public User save(User user) {
        checkUser(user);
        user.setUserId(++id);
        userList.put(user.getUserId(), user);
        return user;
    }

    @Override
    public User put(User user) {
        if (!userList.containsValue(user)) {
            throw new EntityNotFoundException("Пользователя с ID=" + user.getUserId() + " нет в текущем списке!");
        }
        if (user.getUserId() == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        if (user.getName() == null) {
            user.setName(userList.get(user.getUserId()).getName());
        }
        if (user.getEmail() == null) {
            user.setName(userList.get(user.getUserId()).getEmail());
        }
        if (userList.values()
                .stream()
                .filter(user1 -> user1.getEmail().equals(user.getEmail()))
                .allMatch(user1 -> user1.getUserId().equals(user.getUserId()))) {
            if (isValidUser(user)) {
                userList.put(user.getUserId(), user);
            }
        } else throw new UserAlreadyExsistExeption("Пользователь с E-mail=" + user.getEmail() + " уже существует!");


        return user;
    }

    @Override
    public void delete(Long userId) {
        if (!userList.containsKey(userId)) {
            throw new EntityNotFoundException("Пользователя с ID=" + userId + " нет в текущем списке!");
        }
        userList.remove(userId);
    }

    public void checkUser(User user) {
        userList.forEach((aLong, user1) -> {
            if (user1.getEmail().equals(user.getEmail())) {
                throw new CreateDuplicateEmailException("Пользователь с email=" + user.getEmail() + " уже есть!");
            }
        });
    }

    private boolean isValidUser(User user) {
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Некорректный e-mail пользователя: " + user.getEmail());
        }
        if ((user.getName().isEmpty()) || (user.getName().contains(" "))) {
            throw new ValidationException("Некорректный логин пользователя: " + user.getName());
        }
        return true;
    }

}
