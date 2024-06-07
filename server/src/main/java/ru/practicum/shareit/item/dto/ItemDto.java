package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
    private BookingsInItem lastBooking;
    private BookingsInItem nextBooking;
    private List<CommentDto> comments;

    @Getter
    @Setter
    public static class BookingsInItem {
        private Long id;
        private Long bookerId;

        public BookingsInItem(Booking booking) {
            this.id = booking.getId();
            this.bookerId = booking.getBooker().getId();
        }
    }
}