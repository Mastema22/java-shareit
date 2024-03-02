package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    public UserServiceImpl(@Qualifier("UserRepositoryImpl") UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<UserDto> findAllUser() {
        return repository.findAll().stream()
                .map(mapper::toUserDto)
                .collect(toList());
    }

    @Override
    public UserDto findByIdUser(Long id) {
        return mapper.toUserDto(repository.findById(id));
    }

    @Override
    public UserDto addNewUser(UserDto userDto) {
        return mapper.toUserDto(repository.save(mapper.toUser(userDto)));
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        if (userDto.getId() == null) {
            userDto.setId(id);
        }
        return mapper.toUserDto(repository.put(mapper.toUser(userDto)));
    }

    @Override
    public UserDto deleteUser(Long id) {
        return mapper.toUserDto(repository.delete(id));
    }
}