package ru.practicum.shareit.exception;

public class CommentNotAvailableException extends RuntimeException {
    public CommentNotAvailableException(String message) {
        super(message);
    }
}