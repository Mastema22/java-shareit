package ru.practicum.shareit.user.exception;

public class UpdateEmailExistsException extends IllegalArgumentException {
    public UpdateEmailExistsException(String s) {
        super(s);
    }
}
