package gpb.gamesswapservice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "booking")
public class Booking {
    private Long id;
    private Owner oldOwnerId;
    private Owner newOwnerId;
    private Game gameId;
    private String bookingDate;

    protected Booking() {
    }

    public Booking(Owner oldOwnerId, Owner newOwnerId, Game gameId, String bookingDate) {
        this.oldOwnerId = oldOwnerId;
        this.newOwnerId = newOwnerId;
        this.gameId = gameId;
        this.bookingDate = bookingDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "old_owner_id")
    public Owner getOldOwnerId() {
        return oldOwnerId;
    }

    public void setOldOwnerId(Owner oldOwnerId) {
        this.oldOwnerId = oldOwnerId;
    }

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "new_owner_id")
    public Owner getNewOwnerId() {
        return newOwnerId;
    }

    public void setNewOwnerId(Owner newOwnerId) {
        this.newOwnerId = newOwnerId;
    }

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    public Game getGameId() {
        return gameId;
    }

    public void setGameId(Game gameId) {
        this.gameId = gameId;
    }

    @Column(nullable = false, name = "date")
    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }
}
