package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.exception.CreateDuplicateEmailException;
import ru.practicum.shareit.user.exception.UpdateEmailExistsException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final List<User> userList = new ArrayList<>();

    private int id = 0;

    @Override
    public List<User> findAll() {
        return userList;
    }

    @Override
    public User findById(long userId) {
        return userList.stream().filter(user -> user.getId() == userId).findFirst().orElseThrow();
    }

    @Override
    public User save(User user) {
        checkUser(user);
        user.setId(++id);
        userList.add(user);
        return user;
    }

    @Override
    public User put(long id, User user) {
        checkUser(user);
        User userResult = new User();
        for (User user1 : userList) {
            if (user1.getId() == id) {
                if (user.getName() != null ) {
                    if (user1.getName() != user.getName()) {
                        user1.setName(user.getName());
                    } else throw new RuntimeException("Имя уже существует в текущей записи!");
                }
                if (user.getEmail() != null) {
                    if (user1.getEmail() != user.getEmail()) {
                        user1.setEmail(user.getEmail());
                    } else throw new UpdateEmailExistsException("Email уже существует в текущей записи!");
                }
                userResult = user1;
            }
        }
        return userResult;
    }

    @Override
    public void delete(long id) {
        User user = findById(id);
        userList.remove(user);
    }

    public void checkUser(User user) {
        userList.forEach(user1 -> {
            if (user1.getEmail().equals(user.getEmail())) {
                throw new CreateDuplicateEmailException("Email " + user.getEmail() + " уже существует в текущей записи!");
            }
        });

    }
}
