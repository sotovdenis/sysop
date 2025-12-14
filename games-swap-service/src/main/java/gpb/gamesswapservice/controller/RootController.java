package gpb.gamesswapservice.controller;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
public class RootController {

    @GetMapping
    public RepresentationModel<?> getRoot() {
        RepresentationModel<?> rootModel = new RepresentationModel<>();
        rootModel.add(
                linkTo(methodOn(OwnerController.class).getAllOwners()).withRel("owners"),
                linkTo(methodOn(GameController.class).getAllGames(
                        null,
                        0,
                        10)).withRel("games"),
                linkTo(RootController.class).slash("/swagger-ui.html").withRel("documentation")
        );
        return rootModel;
    }
}