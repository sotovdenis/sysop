package gpb.gamesswapservice.service.impl;

import gpb.gamesswapapi.dto.request.BookingRequest;
import gpb.gamesswapapi.dto.response.BookingResponse;
import gpb.gamesswapapi.dto.response.GameResponse;
import gpb.gamesswapapi.dto.response.OwnerResponse;
import gpb.gamesswapservice.entity.Booking;
import gpb.gamesswapservice.entity.Game;
import gpb.gamesswapservice.entity.Owner;
import gpb.gamesswapservice.repository.BookingRepository;
import gpb.gamesswapservice.repository.GameRepository;
import gpb.gamesswapservice.repository.OwnerRepository;
import gpb.gamesswapservice.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final GameRepository gameRepository;
    private final OwnerRepository ownerRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository,
                              GameRepository gameRepository,
                              OwnerRepository ownerRepository) {
        this.bookingRepository = bookingRepository;
        this.gameRepository = gameRepository;
        this.ownerRepository = ownerRepository;
    }

    @Override
    public List<BookingResponse> getBookings() {
        return bookingRepository.findAll().stream().map(b -> new BookingResponse(b.getId(),
                toResponseOwnerResponse(b.getOldOwnerId()),
                toResponseOwnerResponse(b.getNewOwnerId()),

                new GameResponse(
                        b.getGameId().getId(),
                        b.getGameId().getTitle(),
                        b.getGameId().getPrice(),
                        b.getGameId().getDescription(),
                        toResponseOwnerResponse(b.getOldOwnerId())
                ),
                LocalDateTime.now()
        )).collect(Collectors.toList());
    }

    @Override
    public BookingResponse getBooking(Long id) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        Game game = booking.getGameId();

        GameResponse gameResponse = new GameResponse(
                game.getId(),
                game.getTitle(),
                game.getPrice(),
                game.getDescription(),
                toResponseOwnerResponse(booking.getOldOwnerId())
        );

        BookingResponse bookingResponse = new BookingResponse(
                booking.getId(),
                toResponseOwnerResponse(booking.getOldOwnerId()),
                toResponseOwnerResponse(booking.getNewOwnerId()),
                gameResponse,
                LocalDateTime.now()
        );

        return bookingResponse;
    }

    @Override
    public BookingResponse createBooking(BookingRequest booking) {
        Booking bookingToSave = new Booking(
                ownerRepository.findById(booking.oldOwnerId()).get(),
                ownerRepository.findById(booking.newOwnerId()).get(),
                gameRepository.findById(booking.gameId()).get(),
                LocalDateTime.now().toString()
        );

        BookingResponse bookingResponse = new BookingResponse(
                bookingToSave.getId(),
                toResponseOwnerResponse(bookingToSave.getOldOwnerId()),
                toResponseOwnerResponse(bookingToSave.getNewOwnerId()),
                new GameResponse(
                        bookingToSave.getGameId().getId(),
                        bookingToSave.getGameId().getTitle(),
                        bookingToSave.getGameId().getPrice(),
                        bookingToSave.getGameId().getDescription(),
                        toResponseOwnerResponse(bookingToSave.getOldOwnerId())
                ),
                LocalDateTime.now()
        );

        bookingRepository.save(bookingToSave);

        return bookingResponse;
    }

    @Override
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    @Override
    public boolean isGameBooked(Game game) {
        return bookingRepository.findAll()
                .stream()
                .anyMatch(e -> e.getGameId().equals(game.getId()));
    }


    private OwnerResponse toResponseOwnerResponse(Owner owner) {
        return new OwnerResponse(owner.getId(),
                owner.getFirstName(),
                owner.getLastName(),
                owner.getFathersName(),
                owner.getEmail()
        );
    }

    private BookingResponse toResponse(Booking booking) {

        Owner oldOwner = booking.getOldOwnerId();
        Owner newOwner = booking.getNewOwnerId();
        Game game = booking.getGameId();

        OwnerResponse oldOwnerResponse = new OwnerResponse(oldOwner.getId(),
                oldOwner.getFirstName(),
                oldOwner.getLastName(),
                oldOwner.getFathersName(),
                oldOwner.getEmail()
        );

        return new BookingResponse(

                booking.getId(),

                oldOwnerResponse,

                new OwnerResponse(newOwner.getId(),
                        newOwner.getFirstName(),
                        newOwner.getLastName(),
                        newOwner.getFathersName(),
                        newOwner.getEmail()
                ),

                new GameResponse(game.getId(),
                        game.getTitle(),
                        game.getPrice(),
                        game.getDescription(),
                        oldOwnerResponse),

                LocalDateTime.now()
        );
    }
}
