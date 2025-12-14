package gpb.gamesswapapi.api;

import gpb.gamesswapapi.dto.request.OwnerRequest;
import gpb.gamesswapapi.dto.response.OwnerResponse;
import gpb.gamesswapapi.dto.response.StatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "owners", description = "API для работы с владелецами")
@RequestMapping("/api/owners")
public interface OwnerApi {

    @Operation(summary = "Получить всех владельцев")
    @ApiResponse(responseCode = "200", description = "Список владельцев")
    @GetMapping
    CollectionModel<EntityModel<OwnerResponse>> getAllOwners();

    @Operation(summary = "Получить владельца по ID")
    @ApiResponse(responseCode = "200", description = "Владелец найден")
    @ApiResponse(responseCode = "404", description = "Владелец не найден", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @GetMapping("/{id}")
    EntityModel<OwnerResponse> getOwnerById(@PathVariable Long id);

    @Operation(summary = "Создать нового владельца")
    @ApiResponse(responseCode = "201", description = "Владелец успешно создан")
    @ApiResponse(responseCode = "400", description = "Невалидный запрос", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<OwnerResponse>> createOwner(@Valid @RequestBody OwnerRequest request);

    @Operation(summary = "Обновить владельца")
    @ApiResponse(responseCode = "200", description = "Владелец обновлен")
    @ApiResponse(responseCode = "404", description = "Владелец не найден", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @PutMapping("/{id}")
    EntityModel<OwnerResponse> updateOwner(@PathVariable Long id, @Valid @RequestBody OwnerRequest request);

//    @Operation(summary = "Удалить владельца")
//    @ApiResponse(responseCode = "204", description = "Владелец удален")
//    @ApiResponse(responseCode = "404", description = "Владелец не найден")
//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    void deleteOwner(@PathVariable Long id);
}
