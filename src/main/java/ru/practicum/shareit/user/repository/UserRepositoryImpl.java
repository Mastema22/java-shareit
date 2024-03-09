package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;

import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.user.model.User;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;

@Repository("UserRepositoryImpl")
public class UserRepositoryImpl implements UserRepository {

    private final List<User> userList = new ArrayList<>();
    private Long id = 0L;

    @Override
    public List<User> findAll() {
        return userList;
    }

    @Override
    public User findById(Long userId) {
        return userList.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID=" + id + " не найден!"));
    }

    @Override
    public User save(User user) {
        if (userList.stream()
                .noneMatch(user1 -> user1.getEmail().equals(user.getEmail()))) {
            if (!user.getEmail().contains("@") || user.getName().isEmpty() || user.getName().contains(" ")) {
                throw new ValidationException("Некорректный e-mail пользователя: " + user.getEmail());
            }
            user.setId(++id);
            userList.add(user);
        } else throw new UserAlreadyExistsException("Пользователь с E-mail=" + user.getEmail() + " уже существует!");
        return user;
    }

    @Override
    public User put(User user) {
        User existingUser = userList.stream()
                .filter(u -> u.getId().equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID=" + user.getId() + " не найден!"));
        if (user.getName() == null) {
            user.setName(existingUser.getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(existingUser.getEmail());
        }
        existingUser.setName(user.getName());
        if (checkNameEmail(user.getEmail())) {
            existingUser.setEmail(user.getEmail());
        } else {
            throw new CorrectNameEmailException("Некорректный e-mail пользователя: " + user.getEmail());
        }
        return user;
    }

    @Override
    public User delete(Long id) {
        if (id == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        User existingUser = userList.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID=" + id + " не найден!"));
        userList.remove(existingUser);
        return existingUser;

    }

    private boolean checkNameEmail(String email) {
        String[] data = email.split("@");
        return !data[1].contains(data[0]);
    }
}