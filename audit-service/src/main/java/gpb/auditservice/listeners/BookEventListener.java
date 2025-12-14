//package gpb.auditservice.listeners;
//
//import com.rabbitmq.client.Channel;
//import events.BookCreatedEvent;
//import events.BookDeletedEvent;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.rabbit.annotation.*;
//import org.springframework.amqp.support.AmqpHeaders;
//import org.springframework.messaging.handler.annotation.Header;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class BookEventListener {
//    private static final Logger log = LoggerFactory.getLogger(BookEventListener.class);
//    private static final String EXCHANGE_NAME = "books-exchange";
//    private static final String QUEUE_NAME = "notification-queue";
//
//    @RabbitListener(
//            bindings = @QueueBinding(
//                    value = @Queue(
//                            name = QUEUE_NAME,
//                            durable = "true",
//                            // если что-то пойдет не так, отправляем в 'dlx-exchange'
//                            arguments = {
//                                    @Argument(name = "x-dead-letter-exchange", value = "dlx-exchange"),
//                                    @Argument(name = "x-dead-letter-routing-key", value = "dlq.notifications")
//                            }),
//                    exchange = @Exchange(name = EXCHANGE_NAME, type = "topic", durable = "true"),
//                    key = "book.created"
//            )
//    )
//    // Используем @Payload для явного указания параметра сообщения
//    public void handleBookCreatedEvent(@Payload BookCreatedEvent event,
//                                       Channel channel,
//                                       @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
//        try {
//            log.info("Received BookCreatedEvent: {}", event);
//            if (event.title() != null && event.title().equalsIgnoreCase("CRASH")) {
//                throw new RuntimeException("Simulating processing error for DLQ test");
//            }
//            // Логика отправки уведомления...
//            log.info("Notification sent for new book '{}'!", event.title());
//            // Отправляем подтверждение брокеру
//            channel.basicAck(deliveryTag, false);
//
//        } catch (Exception e) {
//            log.error("Failed to process event: {}. Sending to DLQ.", event, e);
//            // Отправляем nack и НЕ просим вернуть в очередь (requeue=false)
//            channel.basicNack(deliveryTag, false, false);
//        }
//    }
//
//    @RabbitListener(
//            bindings = @QueueBinding(
//                    value = @Queue(
//                            name = QUEUE_NAME,
//                            durable = "true",
//                            arguments = {
//                                    @Argument(name = "x-dead-letter-exchange", value = "dlx-exchange"),
//                                    @Argument(name = "x-dead-letter-routing-key", value = "dlq.notifications")
//                            }),
//                    exchange = @Exchange(name = EXCHANGE_NAME, type = "topic", durable = "true"),
//                    key = "book.deleted"
//            )
//    )
//    public void handleBookDeletedEvent(@Payload BookDeletedEvent event, Channel channel,
//                                       @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
//        try {
//            log.info("Received BookDeletedEvent: {}", event);
//            // Логика отмены уведомлений...
//            log.info("Notifications cancelled for deleted bookId {}!", event.bookId());
//            channel.basicAck(deliveryTag, false);
//
//        } catch (Exception e) {
//            log.error("Failed to process event: {}. Sending to DLQ.", event, e);
//            channel.basicNack(deliveryTag, false, false);
//        }
//    }
//
//    @RabbitListener(
//            bindings = @QueueBinding(
//                    value = @Queue(name = "notification-queue.dlq", durable = "true"),
//                    exchange = @Exchange(name = "dlx-exchange", type = "topic", durable = "true"),
//                    key = "dlq.notifications"
//            )
//    )
//    public void handleDlqMessages(Object failedMessage) {
//        log.error("!!! Received message in DLQ: {}", failedMessage);
//        // Здесь может быть логика оповещения администраторов
//    }
//
//
//}
