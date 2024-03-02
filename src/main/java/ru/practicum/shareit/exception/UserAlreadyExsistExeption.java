package ru.practicum.shareit.exception;

public class UserAlreadyExsistExeption extends RuntimeException {
    public UserAlreadyExsistExeption(String s) {
        super(s);
    }
}
