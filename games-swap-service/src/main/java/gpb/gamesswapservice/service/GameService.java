package gpb.gamesswapservice.service;

import gpb.gamesswapapi.dto.request.GameRequest;
import gpb.gamesswapapi.dto.response.GameResponse;
import gpb.gamesswapapi.dto.response.PagedResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GameService {
    GameResponse findGameById(Long id);
    PagedResponse<GameResponse> findAllGames(Long ownerId, int page, int size);
    GameResponse createGame(GameRequest request);
    void deleteGame(Long id);

    GameResponse updateGame(Long id, GameRequest request);
}
