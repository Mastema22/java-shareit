package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> newBooking(Long bookerId, BookingDto inputDto) {
        return post("", bookerId, inputDto);
    }


    public ResponseEntity<Object> bookingRequest(Long ownerId, Long bookingId, boolean approved) {
        return patch("/" + bookingId, ownerId, approved);
    }

    public ResponseEntity<Object> getDataByBooking(Long bookerId, Long bookingId) {
        return get("/" + bookingId, bookerId);
    }

    public ResponseEntity<Object> findAllByBooker(Long bookerId, String state, PageRequest pageRequest) {
        Map<String, Object> parameters = Map.of(
                "state", state,
                "from", pageRequest.getPageNumber(),
                "size", pageRequest.getPageSize()
        );
        return get("?state={state}&from={from}&size={size}", bookerId, parameters);
    }

    public ResponseEntity<Object> findAllByOwner(Long ownerId, String state, PageRequest pageRequest)  {
        Map<String, Object> parameters = Map.of(
                "state", state,
                "from", pageRequest.getPageNumber(),
                "size", pageRequest.getPageSize()
        );
        return get("?state={state}&from={from}&size={size}", ownerId, parameters);
    }

    public ResponseEntity<Object> getBookingsBooker(String state, Long bookerId){
        return get("/owner" + state, bookerId);
    }
}