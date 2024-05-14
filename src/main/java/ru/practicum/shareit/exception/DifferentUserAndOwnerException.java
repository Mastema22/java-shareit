package ru.practicum.shareit.exception;

public class DifferentUserAndOwnerException extends IllegalArgumentException {
    public DifferentUserAndOwnerException(String message) {
        super(message);
    }
}
