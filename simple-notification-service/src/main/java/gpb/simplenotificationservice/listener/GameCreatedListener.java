package gpb.simplenotificationservice.listener;


import events.GameArrivedEvent;
import gpb.simplenotificationservice.handler.NotificationWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class GameCreatedListener {
    private static final Logger log = LoggerFactory.getLogger(GameCreatedListener.class);
    private final NotificationWebSocketHandler webSocketHandler;

    public GameCreatedListener(NotificationWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @RabbitListener(queues = "game.created")
    public void handleOwnerRatedEvent(GameArrivedEvent event) {
        log.info("Получено событие (пожаловала новая игра): gameId={}, title={}", event.gameId(), event.title());

        if (event.userIdsLiked() == null || event.userIdsLiked().isEmpty()) {
            log.warn("Пропущено событие: список userIdsLiked пуст или null");
            return;
        }

        String message =
                String.format("Ваша любимая, по какой-то из причин, игра появилась в стэке игр! Игра: %s", event.title());

        webSocketHandler.sendToUsers(event.userIdsLiked(), message);
    }
}
