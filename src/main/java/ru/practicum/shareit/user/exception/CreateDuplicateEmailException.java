package ru.practicum.shareit.user.exception;

public class CreateDuplicateEmailException extends IllegalArgumentException {
    public CreateDuplicateEmailException(String s) {
        super(s);
    }
}
