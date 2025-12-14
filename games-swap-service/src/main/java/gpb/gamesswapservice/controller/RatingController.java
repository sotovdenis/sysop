package gpb.gamesswapservice.controller;

import events.GameValuedEvent;
import gpb.analyticsservice.AnalyticsServiceGrpc;
import gpb.analyticsservice.GameValueRequest;
import gpb.gamesswapservice.config.RabbitMQConfig;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
public class RatingController {

    @GrpcClient("analytics-service")
    private AnalyticsServiceGrpc.AnalyticsServiceBlockingStub analyticsStub;

    private final RabbitTemplate rabbitTemplate;

    public RatingController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/api/games/{gameId}/value")
    public String valueGame(@PathVariable Long gameId,
                            @RequestParam Long ownerId,
                            @RequestParam(defaultValue = "General") String category) {
        try {
            // Вызов gRPC для оценки игры
            var request = GameValueRequest.newBuilder()
                    .setGameId(gameId)
                    .setOwnerId(ownerId)
                    .setCategory(category)
                    .build();

            var gRpcResponse = analyticsStub.calculateGameValue(request);

            // Отправка события в Fanout-обмен
            var event = new GameValuedEvent(
                    gRpcResponse.getGameId(),
                    gRpcResponse.getValueScore(),
                    gRpcResponse.getVerdict(),
                    gRpcResponse.getOwnerId()
            );

            rabbitTemplate.convertAndSend(RabbitMQConfig.FANOUT_EXCHANGE, "", event);

            return "Game value calculated: " + gRpcResponse.getValueScore();

        } catch (Exception e) {
            return "Game value calculation failed";
        }
    }
}