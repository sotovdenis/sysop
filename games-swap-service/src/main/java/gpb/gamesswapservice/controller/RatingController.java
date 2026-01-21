package gpb.gamesswapservice.controller;

import gpb.gamesswapapi.dto.response.GameValueResponse;
import gpb.gamesswapservice.service.GameService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
public class RatingController {

    private final GameService gameService;

    public RatingController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/{gameId}/value")
    public GameValueResponse valueGame(
            @PathVariable Long gameId,
            @RequestParam Long ownerId,
            @RequestParam(defaultValue = "General") String category) {

        return gameService.calculateGameValue(gameId, ownerId, category);
    }
}
