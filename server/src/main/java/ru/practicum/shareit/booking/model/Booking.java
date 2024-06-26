package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Bookings")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date")
    private LocalDateTime start;
    @Column(name = "end_date")
    private LocalDateTime end;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "booker_id", referencedColumnName = "id")
    private User booker;
    @Enumerated(EnumType.STRING)
    private Status status;

    public static State getState(Booking booking) {
        switch (booking.getStatus()) {
            case REJECTED:
            case CANCELLED:
                return State.REJECTED;
            case WAITING:
            case APPROVED:
                return chooseTime(booking);
        }
        return null;
    }

    private static State chooseTime(Booking booking) {
        if (booking.getStart().isAfter(LocalDateTime.now())) {
            return State.FUTURE;
        } else if (booking.getEnd().isBefore(LocalDateTime.now())) {
            return State.PAST;
        } else {
            return State.CURRENT;
        }
    }
}