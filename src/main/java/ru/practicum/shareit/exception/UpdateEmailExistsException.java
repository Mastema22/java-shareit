package ru.practicum.shareit.exception;

public class UpdateEmailExistsException extends IllegalArgumentException {
    public UpdateEmailExistsException(String s) {
        super(s);
    }
}
