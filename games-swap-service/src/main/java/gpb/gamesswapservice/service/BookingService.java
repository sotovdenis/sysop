package gpb.gamesswapservice.service;

import gpb.gamesswapapi.dto.request.BookingRequest;
import gpb.gamesswapapi.dto.response.BookingResponse;
import gpb.gamesswapservice.entity.Booking;
import gpb.gamesswapservice.entity.Game;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookingService {
    List<BookingResponse> getBookings();
    BookingResponse getBooking(Long id);
    BookingResponse createBooking(BookingRequest booking);
    void deleteBooking(Long id);
    boolean isGameBooked(Game game);
}
