package gpb.gamesswapservice.assemblers;

import gpb.gamesswapapi.dto.response.GameResponse;
import gpb.gamesswapservice.controller.GameController;
import gpb.gamesswapservice.controller.OwnerController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GameModelAssembler implements RepresentationModelAssembler<GameResponse, EntityModel<GameResponse>> {

    @Override
    public EntityModel<GameResponse> toModel(GameResponse game) {
        return EntityModel.of(game,
                linkTo(methodOn(GameController.class).getGame(game.getId())).withSelfRel(),
                linkTo(methodOn(OwnerController.class).getOwnerById(game.getOwner().getId())).withRel("owner"),
                linkTo(methodOn(GameController.class).getAllGames(null, 0, 10)).withRel("collection")
        );
    }
}
