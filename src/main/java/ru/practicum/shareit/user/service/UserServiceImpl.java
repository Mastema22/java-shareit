package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.CorrectNameEmailException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public List<UserDto> findAllUser() {
        return repository.findAll().stream()
                .map(mapper::toUserDto)
                .collect(toList());
    }

    @Override
    public UserDto findByIdUser(Long id) {
        return mapper.toUserDto(repository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь с ID=" + id + " не найден!")));
    }

    @Override
    public UserDto addNewUser(UserDto userDto) {
        return mapper.toUserDto(repository.save(mapper.toUser(userDto)));
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = mapper.toUser(userDto);
        User existingUser = repository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь с ID= " + id + " не найден!"));
        if (user.getName() == null) user.setName(existingUser.getName());
        if (user.getEmail() == null) user.setEmail(existingUser.getEmail());
        existingUser.setName(user.getName());
        if (checkNameEmail(user.getEmail())) {
            existingUser.setEmail(user.getEmail());
        } else {
            throw new CorrectNameEmailException("Некорректный e-mail пользователя: " + user.getEmail());
        }
        return mapper.toUserDto(repository.save(existingUser));
    }

    @Override
    public void deleteUser(Long id) {
        repository.deleteUserById(id);
    }

    private boolean checkNameEmail(String email) {
        String[] data = email.split("@");
        return !data[1].contains(data[0]);
    }
}