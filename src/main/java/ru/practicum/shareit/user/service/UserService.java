package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<User> findAllUser();

    User findByIdUser(long id);

    User addNewUser(User user);

    User updateUser(long id, User user);

    void deleteUser(long id);
}