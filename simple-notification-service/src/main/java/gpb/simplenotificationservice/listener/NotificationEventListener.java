package gpb.simplenotificationservice.listener;

import events.GameValuedEvent;
import gpb.simplenotificationservice.handler.NotificationWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationEventListener.class);
    private final NotificationWebSocketHandler webSocketHandler;

    public NotificationEventListener(NotificationWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @RabbitListener(queues = "notification-queue.game-valued")
    public void handleGameValuedEvent(GameValuedEvent event) {
        log.info("Получено событие оценки игры: gameId={}, value={}, ownerId={}",
                event.gameId(), event.valueScore(), event.ownerId());

        String message = String.format("Ваша игра оценена с помощью ГигаЧата! Стоимость: %d (%s)", event.valueScore(), event.verdict());
        webSocketHandler.sendToUser(event.ownerId(), message);
    }
}