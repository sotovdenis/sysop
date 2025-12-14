package gpb.gamesswapservice.assemblers;

import gpb.gamesswapapi.dto.response.BookingResponse;
import gpb.gamesswapservice.controller.GameController;
import gpb.gamesswapservice.controller.OwnerController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BookingModelAssembler implements RepresentationModelAssembler<BookingResponse, EntityModel<BookingResponse>> {

    @Override
    public EntityModel<BookingResponse> toModel(BookingResponse bookingResponse) {
        return EntityModel.of(bookingResponse,
                linkTo(methodOn(GameController.class).getGame(bookingResponse.getGame().getId())).withSelfRel(),
                linkTo(methodOn(OwnerController.class).getOwnerById(bookingResponse.getNewOwner().getId())).withRel("newOwner"),
                linkTo(methodOn(OwnerController.class).getOwnerById(bookingResponse.getOldOwner().getId())).withRel("oldOwner"),
                linkTo(methodOn(GameController.class).getAllGames(null, 0, 10)).withRel("collection")
        );
    }
}
