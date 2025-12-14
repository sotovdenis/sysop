package gpb.gamesswapservice.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "owner")
public class Owner {

    private Long id;
    private String firstName;
    private String lastName;
    private String fathersName;
    private String email;
    private List<Game> games = new ArrayList<>();

    protected Owner() {}

    public Owner(String firstName, String lastName, String fathersName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fathersName = fathersName;
        this.email = email;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(nullable = false)
    public String getFirstName() {
        return firstName;
    }

    @Column(nullable = false)
    public String getLastName() {
        return lastName;
    }

    @Column(nullable = false)
    public String getFathersName() {
        return fathersName;
    }

    @Column(unique = true, nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @OneToMany(mappedBy = "owner")
    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public void addGame(Game game) {
        games.add(game);
        game.setOwner(this);
    }

    public void removeGame(Game game) {
        games.remove(game);
        game.setOwner(null);
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFathersName(String fathersName) {
        this.fathersName = fathersName;
    }
}
