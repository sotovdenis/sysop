package gpb.gamesswapservice.service.impl;

import events.GameArrivedEvent;
import events.GameCreatedEvent;
import events.GameDeletedEvent;
import gpb.gamesswapapi.dto.request.GameRequest;
import gpb.gamesswapapi.dto.response.GameResponse;
import gpb.gamesswapapi.dto.response.OwnerResponse;
import gpb.gamesswapapi.dto.response.PagedResponse;
import gpb.gamesswapapi.exception.ResourceNotFoundException;
import gpb.gamesswapservice.config.RabbitMQConfig;
import gpb.gamesswapservice.entity.Game;
import gpb.gamesswapservice.entity.Owner;
import gpb.gamesswapservice.repository.GameRepository;
import gpb.gamesswapservice.repository.OwnerRepository;
import gpb.gamesswapservice.service.GameService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final OwnerRepository ownerRepository;
    private final RabbitTemplate rabbitTemplate;

    public GameServiceImpl(GameRepository gameRepository,
                           OwnerRepository ownerRepository,
                           RabbitTemplate rabbitTemplate) {
        this.gameRepository = gameRepository;
        this.ownerRepository = ownerRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public GameResponse findGameById(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Game", id));

        Owner owner = game.getOwner();
        OwnerResponse ownerResponse = toOwnerResponse(owner);

        return new GameResponse(
                game.getId(),
                game.getTitle(),
                game.getPrice(),
                game.getDescription(),
                ownerResponse
        );
    }

    @Override
    public GameResponse createGame(GameRequest request) {
        Owner owner = ownerRepository.findById(request.ownerId())
                .orElseThrow(() -> new ResourceNotFoundException("Owner", request.ownerId()));

        Game game = new Game(
                request.title(),
                request.price(),
                request.description(),
                owner
        );

        Game savedGame = gameRepository.save(game);

        List<Long> demoOwnerIds = List.of(1L, 2L, 3L, 123L);

        GameArrivedEvent arrivedEvent = new GameArrivedEvent(
                savedGame.getId(),
                savedGame.getTitle(),
                demoOwnerIds
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY_GAME_CREATED,
                arrivedEvent
        );

        return toGameResponse(savedGame);
    }

    @Override
    public GameResponse updateGame(Long id, GameRequest request) {
        Game existingGame = gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Game", id));

        existingGame.setTitle(request.title());
        existingGame.setPrice(request.price());
        existingGame.setDescription(request.description());

        Game updatedGame = gameRepository.save(existingGame);
        return toGameResponse(updatedGame);
    }

    @Override
    public void deleteGame(Long id) {
        if (!gameRepository.existsById(id)) {
            throw new ResourceNotFoundException("Game", id);
        }

        GameDeletedEvent event = new GameDeletedEvent(id);

        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_GAME_DELETED, event);

        gameRepository.deleteById(id);
    }

    @Override
    public PagedResponse<GameResponse> findAllGames(Long ownerId, int page, int size) {
        Stream<GameResponse> gamesStream = gameRepository.findAll().stream()
                .map(this::toGameResponse)
                .sorted(Comparator.comparing(GameResponse::getId));

        if (ownerId != null) {
            gamesStream = gamesStream.filter(game -> game.getOwner() != null && game.getOwner().getId().equals(ownerId));
        }

        List<GameResponse> allGames = gamesStream.toList();
        int totalElements = allGames.size();

        if (totalElements == 0 || page < 0 || size <= 0) {
            return new PagedResponse<>(List.of(), page, size, totalElements, 0, true);
        }

        int totalPages = (int) Math.ceil((double) totalElements / size);
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, totalElements);

        List<GameResponse> pageContent = (fromIndex >= totalElements)
                ? List.of()
                : allGames.subList(fromIndex, toIndex);

        boolean last = page >= totalPages - 1;

        return new PagedResponse<>(pageContent, page, size, totalElements, totalPages, last);
    }

    private OwnerResponse toOwnerResponse(Owner owner) {
        return new OwnerResponse(
                owner.getId(),
                owner.getFirstName(),
                owner.getLastName(),
                owner.getFathersName(),
                owner.getEmail()
        );
    }

    private GameResponse toGameResponse(Game game) {
        OwnerResponse ownerResponse = toOwnerResponse(game.getOwner());
        return new GameResponse(
                game.getId(),
                game.getTitle(),
                game.getPrice(),
                game.getDescription(),
                ownerResponse
        );
    }
}