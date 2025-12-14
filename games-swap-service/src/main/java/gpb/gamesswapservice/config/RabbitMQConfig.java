package gpb.gamesswapservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String OWNER_RATED_QUEUE = "notification-queue.owner-rated";
    public static final String GAME_VALUED_QUEUE = "notification-queue.game-valued";
    public static final String FANOUT_EXCHANGE = "analytics-fanout";
    public static final String EXCHANGE_NAME = "games-exchange";
    public static final String ROUTING_KEY_GAME_CREATED = "game.created";
    public static final String ROUTING_KEY_GAME_DELETED = "game.deleted";

    @Bean
    public Binding gameCreatedBinding() {
        return BindingBuilder
                .bind(gameArrivedQueue())
                .to(gamesExchange())
                .with(ROUTING_KEY_GAME_CREATED);
    }

    @Bean
    public TopicExchange gamesExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public FanoutExchange analyticsExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE, true, false);
    }

    @Bean
    public Queue gameValuedQueue() {
        return QueueBuilder.durable(GAME_VALUED_QUEUE).build();
    }

    @Bean
    public Binding ownerRatedBinding() {
        return BindingBuilder.bind(ownerRatedQueue()).to(analyticsExchange());
    }

    @Bean
    public Queue ownerRatedQueue() {
        return QueueBuilder.durable(OWNER_RATED_QUEUE).build();
    }

    @Bean
    public Queue gameArrivedQueue() {
        return QueueBuilder.durable(ROUTING_KEY_GAME_CREATED).build();
    }

    @Bean
    public Binding gameValuedBinding() {
        return BindingBuilder.bind(gameValuedQueue()).to(analyticsExchange());
    }

    @Bean
    public TopicExchange booksExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }
}