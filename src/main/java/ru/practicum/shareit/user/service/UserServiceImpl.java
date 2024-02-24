package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<User> findAllUser() {
        return repository.findAll();
    }

    @Override
    public User findByIdUser(long id) {
        return repository.findById(id);
    }

    @Override
    public User addNewUser(User user) {
        return repository.save(user);
    }

    @Override
    public User updateUser(long id, User user) {
        return repository.put(id, user);
    }

    @Override
    public void deleteUser(long id) {
        repository.delete(id);
    }
}
