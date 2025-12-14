package gpb.gamesswapapi.dto.response;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Objects;

@Relation(collectionRelation = "games", itemRelation = "game")
public class GameResponse extends RepresentationModel<GameResponse> {

    private final Long id;
    private final String title;
    private final Double price;
    private final String description;
    private final OwnerResponse owner;

    public GameResponse(Long id, String title, Double price, String description, OwnerResponse owner) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public OwnerResponse getOwner() {
        return owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GameResponse that = (GameResponse) o;
        return Objects.equals(id, that.id)
                && Objects.equals(title, that.title)
                && Objects.equals(price, that.price)
                && Objects.equals(description, that.description)
                && Objects.equals(owner, that.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, title, price, description, owner);
    }
}