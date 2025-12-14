package gpb.gamesswapservice.graphql;

import com.netflix.graphql.dgs.*;
import gpb.gamesswapapi.dto.request.GameRequest;
import gpb.gamesswapapi.dto.response.GameResponse;
import gpb.gamesswapapi.dto.response.OwnerResponse;
import gpb.gamesswapapi.dto.response.PagedResponse;
import gpb.gamesswapservice.service.GameService;
import graphql.schema.DataFetchingEnvironment;

import java.util.Map;

@DgsComponent
public class GameDataFetcher {

    private final GameService gameService;

    public GameDataFetcher(GameService gameService) {
        this.gameService = gameService;
    }

    @DgsQuery
    public GameResponse gameById(@InputArgument Long id) {
        return gameService.findGameById(id);
    }

    @DgsQuery
    public PagedResponse<GameResponse> games(
            @InputArgument Long ownerId,
            @InputArgument int page,
            @InputArgument int size) {
        return gameService.findAllGames(ownerId, page, size);
    }

    // Разрешает вложенное поле 'owner' внутри типа 'Game'
    @DgsData(parentType = "Game", field = "owner")
    public OwnerResponse owner(DataFetchingEnvironment dfe) {
        GameResponse game = dfe.getSource();
        return game.getOwner();
    }

    @DgsMutation
    public GameResponse createGame(@InputArgument("input") Map<String, Object> input) {
        Long ownerId = input.get("ownerId") != null
                ? Long.parseLong(input.get("ownerId").toString())
                : null;

        GameRequest request = new GameRequest(
                (String) input.get("title"),
                (Double) input.get("price"),
                (String) input.get("description"),
                ownerId
        );
        return gameService.createGame(request);
    }

    @DgsMutation
    public GameResponse updateGame(
            @InputArgument Long id,
            @InputArgument("input") Map<String, String> input) {

        GameRequest request = new GameRequest(
                input.get("title"),
                Double.parseDouble(input.get("price")),
                input.get("description"),
                null // ownerId не обновляется через этот мутатор
        );
        return gameService.updateGame(id, request);
    }

    @DgsMutation
    public Long deleteGame(@InputArgument Long id) {
        gameService.deleteGame(id);
        return id;
    }
}