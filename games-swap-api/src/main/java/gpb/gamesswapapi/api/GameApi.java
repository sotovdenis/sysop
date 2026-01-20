package gpb.gamesswapapi.api;

import gpb.gamesswapapi.dto.request.BookingRequest;
import gpb.gamesswapapi.dto.request.GameRequest;
import gpb.gamesswapapi.dto.response.BookingResponse;
import gpb.gamesswapapi.dto.response.GameResponse;
import gpb.gamesswapapi.dto.response.StatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.*;

import java.util.List;

@Tag(name = "games", description = "API для работы с добавлением новой игры в систему обмена")
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "Ошибка валидации", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = StatusResponse.class))
        }),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = StatusResponse.class))
        })
})
@RequestMapping("/api/games")
public interface GameApi {

    @Operation(summary = "Получить список всех игр с фильтрацией и пагинацией")
    @ApiResponse(responseCode = "200", description = "Список игр")
    @GetMapping("/all")
    PagedModel<EntityModel<GameResponse>> getAllGames(
            @Parameter(description = "Фильтр по ID владельца") @RequestParam(required = false) Long ownerId,
            @Parameter(description = "Номер страницы (0..N)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы") @RequestParam(defaultValue = "10") int size
    );

    @Operation(summary = "Создать новую игру")
    @ApiResponse(responseCode = "201", description = "Игра успешно создана")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    EntityModel<GameResponse> createGame(@Valid @RequestBody GameRequest request);

    @Operation(summary = "Забронировать игру среди других пользователей")
    @ApiResponse(responseCode = "201", description = "Игра успешно забронирована")
    @PostMapping(value = "/book", consumes = MediaType.APPLICATION_JSON_VALUE)
    EntityModel<BookingResponse> bookGame(@RequestBody BookingRequest request);

    @Operation(summary = "Получить игру по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Игра найдена"),
            @ApiResponse(responseCode = "404", description = "Игра не найдена", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StatusResponse.class))
            })
    })
    @GetMapping("/{id}")
    EntityModel<GameResponse> getGame(@PathVariable Long id);

    @Operation(summary = "Удалить игру по ID")
    @ApiResponse(responseCode = "204", description = "Игра успешно удалена")
    @ApiResponse(responseCode = "404", description = "Игра не найдена")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteGame(@PathVariable Long id);
}
