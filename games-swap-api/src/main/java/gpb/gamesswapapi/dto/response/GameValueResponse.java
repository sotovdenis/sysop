package gpb.gamesswapapi.dto.response;

public record GameValueResponse(
        Long gameId,
        Integer valueScore,
        String verdict,
        Long ownerId
) {}

