package ru.practicum.shareit.exception;

public class CreateDuplicateEmailException extends IllegalArgumentException {
    public CreateDuplicateEmailException(String s) {
        super(s);
    }
}
