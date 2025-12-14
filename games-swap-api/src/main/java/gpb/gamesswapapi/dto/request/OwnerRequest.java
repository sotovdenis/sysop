package gpb.gamesswapapi.dto.request;

import jakarta.validation.constraints.NotBlank;

public record OwnerRequest(
        @NotBlank(message = "Имя владельца не может быть пустым") String firstName,
        @NotBlank(message = "Фамилия владельца не может быть пустой") String lastName,
        @NotBlank(message = "Отчество владельца не может быть пустой") String fathersName,
        @NotBlank(message = "Почта владельца не может быть пустой") String email
        ) {}
