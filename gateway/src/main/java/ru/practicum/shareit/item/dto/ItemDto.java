package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
    }
}