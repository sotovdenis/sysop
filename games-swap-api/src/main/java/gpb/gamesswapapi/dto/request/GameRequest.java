package gpb.gamesswapapi.dto.request;

import jakarta.validation.constraints.*;

public record GameRequest(
        @NotBlank(message = "Название не может быть пустым") String title,
        Double price,
        @Size(min = 10, max = 255, message = "Описание игры может быть от 10 символов до 255") String description,
        Long ownerId
) {}
