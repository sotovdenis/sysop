package gpb.auditservice.listeners;

import com.rabbitmq.client.Channel;
import events.GameCreatedEvent;
import events.GameDeletedEvent;
import events.GameValuedEvent;
import events.OwnerRatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GameEventListener {
    private static final Logger log = LoggerFactory.getLogger(GameEventListener.class);
    private static final String EXCHANGE_NAME = "games-exchange";
    private static final String CREATED_QUEUE = "notification-queue.created";
    private static final String DELETED_QUEUE = "notification-queue.deleted";

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(
                            name = CREATED_QUEUE,
                            durable = "true",
                            // если что-то пойдет не так, отправляем в 'dlx-exchange'
                            arguments = {
                                    @Argument(name = "x-dead-letter-exchange", value = "dlx-exchange"),
                                    @Argument(name = "x-dead-letter-routing-key", value = "dlq.notifications")
                            }),
                    exchange = @Exchange(name = EXCHANGE_NAME, type = "topic", durable = "true"),
                    key = "game.created"
            )
    )
    // Используем @Payload для явного указания параметра сообщения
    public void handleGameCreatedEvent(@Payload GameCreatedEvent event,
                                       Channel channel,
                                       @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            log.info("Received GameCreatedEvent: {}", event);
            if (event.title() != null && event.title().equalsIgnoreCase("CRASH")) {
                throw new RuntimeException("Simulating processing error for DLQ test");
            }
            // Логика отправки уведомления...
            log.info("Notification sent for new game '{}'!", event.title());
            // Отправляем подтверждение брокеру
            channel.basicAck(deliveryTag, false);

        } catch (Exception e) {
            log.error("Failed to process event: {}. Sending to DLQ.", event, e);
            // Отправляем nack и НЕ просим вернуть в очередь (requeue=false)
            channel.basicNack(deliveryTag, false, false);
        }
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(
                            name = DELETED_QUEUE,
                            durable = "true",
                            arguments = {
                                    @Argument(name = "x-dead-letter-exchange", value = "dlx-exchange"),
                                    @Argument(name = "x-dead-letter-routing-key", value = "dlq.notifications")
                            }),
                    exchange = @Exchange(name = EXCHANGE_NAME, type = "topic", durable = "true"),
                    key = "game.deleted"
            )
    )
    public void handleGameDeletedEvent(@Payload GameDeletedEvent event, Channel channel,
                                       @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            log.info("Received GameDeletedEvent: {}", event);
            log.info("Notifications cancelled for deleted gameId {}!", event.gameId());
            channel.basicAck(deliveryTag, false);

        } catch (Exception e) {
            log.error("Failed to process event: {}. Sending to DLQ.", event, e);
            channel.basicNack(deliveryTag, false, false);
        }
    }

//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(name = "game-deleted-queue", durable = "true"),
//            exchange = @Exchange(name = "games-exchange", type = "topic"),
//            key = "game.deleted"
//    ))
//    public void handleGameDeletedEvent(GameDeletedEvent event) {
//        log.info("Game with id={} was deleted.", event);
//    }
//
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(name = "q.audit.analytics", durable = "true"),
//            exchange = @Exchange(name = "analytics-fanout", type = "fanout")
//    ))
//    public void handleRating(OwnerRatedEvent event) {
//        log.info("NOTIFY: Sending email. User {} has new rating: {}", event.userId(), event.score());
//    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "q.audit.game-valued", durable = "true"),
                    exchange = @Exchange(name = "analytics-fanout", type = "fanout")
            )
    )
    public void handleGameValuation(GameValuedEvent event) {
        log.info("AUDIT: Game {} valued at {} for owner {}",
                event.gameId(), event.valueScore(), event.ownerId());
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "notification-queue.dlq", durable = "true"),
                    exchange = @Exchange(name = "dlx-exchange", type = "topic", durable = "true"),
                    key = "dlq.notifications"
            )
    )
    public void handleDlqMessages(Object failedMessage) {
        log.error("!!! Received message in DLQ: {}", failedMessage);
        // Здесь может быть логика оповещения администраторов
    }
}
