package gpb.gamesswapapi.dto.response;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.Objects;

@Relation(collectionRelation = "bookings", itemRelation = "booking")
public class BookingResponse extends RepresentationModel<BookingResponse> {

    private final Long id;
    private final OwnerResponse oldOwner;
    private final OwnerResponse newOwner;
    private final GameResponse game;
    private final LocalDateTime bookingDate;

    public BookingResponse(
            Long id,
            OwnerResponse oldOwner,
            OwnerResponse newOwner,
            GameResponse game,
            LocalDateTime bookingDate) {
        this.id = id;
        this.oldOwner = oldOwner;
        this.newOwner = newOwner;
        this.game = game;
        this.bookingDate = bookingDate;
    }

    public Long getId() {
        return id;
    }

    public OwnerResponse getOldOwner() {
        return oldOwner;
    }

    public OwnerResponse getNewOwner() {
        return newOwner;
    }

    public GameResponse getGame() {
        return game;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingResponse that = (BookingResponse) o;
        return Objects.equals(id, that.id)
                && Objects.equals(oldOwner, that.oldOwner)
                && Objects.equals(newOwner, that.newOwner)
                && Objects.equals(game, that.game)
                && Objects.equals(bookingDate, that.bookingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, oldOwner, newOwner, game, bookingDate);
    }
}