package ru.practicum.shareit.exception;

public class CorrectNameEmailException extends RuntimeException {
    public CorrectNameEmailException(String message) {
        super(message);
    }
}
