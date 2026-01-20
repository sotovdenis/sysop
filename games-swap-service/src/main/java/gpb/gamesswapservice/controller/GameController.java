package gpb.gamesswapservice.controller;

import events.GameArrivedEvent;
import events.OwnerRatedEvent;
import gpb.gamesswapapi.api.GameApi;
import gpb.gamesswapapi.dto.request.BookingRequest;
import gpb.gamesswapapi.dto.request.GameRequest;
import gpb.gamesswapapi.dto.response.BookingResponse;
import gpb.gamesswapapi.dto.response.GameResponse;
import gpb.gamesswapapi.dto.response.PagedResponse;
import gpb.gamesswapservice.assemblers.BookingModelAssembler;
import gpb.gamesswapservice.assemblers.GameModelAssembler;
import gpb.gamesswapservice.config.RabbitMQConfig;
import gpb.gamesswapservice.service.BookingService;
import gpb.gamesswapservice.service.GameService;
import jakarta.validation.Valid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class GameController implements GameApi {

    private final GameModelAssembler gameModelAssembler;
    private final PagedResourcesAssembler<GameResponse> pagedResourcesAssembler;
    private final GameService gameService;
    private final BookingService bookingService;
    private final BookingModelAssembler bookingModelAssembler;

    @Autowired
    public GameController(GameModelAssembler gameModelAssembler,
                          PagedResourcesAssembler<GameResponse> pagedResourcesAssembler,
                          GameService gameService, BookingService bookingService,
                          BookingModelAssembler bookingModelAssembler) {

        this.gameModelAssembler = gameModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.gameService = gameService;
        this.bookingService = bookingService;
        this.bookingModelAssembler = bookingModelAssembler;
    }

    @Override
    public PagedModel<EntityModel<GameResponse>> getAllGames(Long ownerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponse<GameResponse> pagedResponse = gameService.findAllGames(
                ownerId,
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        Page<GameResponse> gamePage = new PageImpl<>(
                pagedResponse.content(),
                PageRequest.of(pagedResponse.pageNumber(), pagedResponse.pageSize()),
                pagedResponse.totalElements()
        );

        return pagedResourcesAssembler.toModel(gamePage, gameModelAssembler);
    }

    @Override
    public EntityModel<GameResponse> createGame(@Valid GameRequest request) {
        GameResponse gameResponse = gameService.createGame(request);
        return gameModelAssembler.toModel(gameResponse);
    }

    @Override
    public EntityModel<BookingResponse> bookGame(@Valid BookingRequest request) {

        BookingResponse booking = bookingService.createBooking(request);

        return bookingModelAssembler.toModel(booking);
    }

    @Override
    public EntityModel<GameResponse> getGame(Long id) {

        GameResponse gameResponse = gameService.findGameById(id);

        return gameModelAssembler.toModel(gameResponse);
    }

    @Override
    public void deleteGame(Long id) {
        gameService.deleteGame(id);
    }
}
