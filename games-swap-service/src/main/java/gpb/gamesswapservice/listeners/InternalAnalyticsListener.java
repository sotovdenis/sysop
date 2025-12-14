package gpb.gamesswapservice.listeners;

import events.GameValuedEvent;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class InternalAnalyticsListener {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "q.gamesswapservice.analytics.log", durable = "true"),
            exchange = @Exchange(name = "analytics-fanout", type = "fanout")))
    public void logGameValuation(GameValuedEvent event) {
        System.out.println("С help ГИГАЧАТА была произведена оценка ваших details " + event.gameId() + " для челика " + event.ownerId());
    }
}