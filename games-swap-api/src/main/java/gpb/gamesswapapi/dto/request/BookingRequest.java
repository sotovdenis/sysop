package gpb.gamesswapapi.dto.request;

import jakarta.validation.constraints.NotNull;

public record BookingRequest(
        @NotNull(message = "ID игры не может быть пустым") Long gameId,
        @NotNull(message = "ID старого владельца не может быть пустым") Long oldOwnerId,
        @NotNull(message = "ID нового владельца не может быть пустым") Long newOwnerId
) {}