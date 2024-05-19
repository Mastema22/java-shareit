package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Booking;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemDto {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
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