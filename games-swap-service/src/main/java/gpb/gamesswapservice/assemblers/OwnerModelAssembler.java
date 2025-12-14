package gpb.gamesswapservice.assemblers;


import gpb.gamesswapapi.dto.response.OwnerResponse;
import gpb.gamesswapservice.controller.GameController;
import gpb.gamesswapservice.controller.OwnerController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OwnerModelAssembler implements RepresentationModelAssembler<OwnerResponse, EntityModel<OwnerResponse>> {

    @Override
    public EntityModel<OwnerResponse> toModel(OwnerResponse owner) {
        return EntityModel.of(owner,
                linkTo(methodOn(OwnerController.class).getOwnerById(owner.getId())).withSelfRel(),
                linkTo(methodOn(GameController.class).getAllGames(owner.getId(), 0, 10)).withRel("games"),
                linkTo(methodOn(OwnerController.class).getAllOwners()).withRel("collection")
        );
    }

    @Override
    public CollectionModel<EntityModel<OwnerResponse>> toCollectionModel(Iterable<? extends OwnerResponse> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities)
                .add(linkTo(methodOn(OwnerController.class).getAllOwners()).withSelfRel());
    }
}