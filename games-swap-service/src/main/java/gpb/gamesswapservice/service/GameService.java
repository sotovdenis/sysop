package gpb.gamesswapservice.service;

import gpb.gamesswapapi.dto.request.GameRequest;
import gpb.gamesswapapi.dto.response.GameResponse;
import gpb.gamesswapapi.dto.response.GameValueResponse;
import gpb.gamesswapapi.dto.response.PagedResponse;

public interface GameService {

    PagedResponse<GameResponse> findAllGames(Long ownerId, int page, int size);

    GameResponse findGameById(Long id);

    GameResponse createGame(GameRequest request);

    GameResponse updateGame(Long id, GameRequest request);

    void deleteGame(Long id);

    GameValueResponse calculateGameValue(Long gameId, Long ownerId, String category);
}
