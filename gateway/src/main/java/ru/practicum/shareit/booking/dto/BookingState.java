package ru.practicum.shareit.booking.dto;

import java.util.Optional;

/**
 * Перечисление {@code BookingState} представляет состояния бронирования.
 * <ul>
 * <li>{@link BookingState#ALL} - все бронирования.
 * <li>{@link BookingState#CURRENT} - текущие бронирования.
 * <li>{@link BookingState#FUTURE} - будущие бронирования.
 * <li>{@link BookingState#PAST} - завершенные бронирования.
 * <li>{@link BookingState#REJECTED} - отклоненные бронирования.
 * <li>{@link BookingState#WAITING} - бронирования, ожидающие подтверждения.
 */
public enum BookingState {
    /**
     * {@code ALL}  - все бронирования.
     */
    ALL,
    /**
     * {@code CURRENT} - текущие бронирования.
     */
    CURRENT,
    /**
     * {@code FUTURE} - будущие бронирования.
     */
    FUTURE,
    /**
     * {@code PAST} - завершенные бронирования.
     */
    PAST,
    /**
     * {@code REJECTED} - отклоненные бронирования.
     */
    REJECTED,
    /**
     * {@code WAITING} - бронирования, ожидающие подтверждения.
     */
    WAITING;

    public static Optional<BookingState> from(String stringState) {
        for (BookingState state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}