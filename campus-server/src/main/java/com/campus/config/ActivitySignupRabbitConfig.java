package com.campus.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;

@Configuration
@Slf4j
public class ActivitySignupRabbitConfig {

    public static final String ACTIVITY_SIGNUP_EXCHANGE = "activity.signup.direct";
    public static final String ACTIVITY_SIGNUP_QUEUE = "activity.signup.queue";
    public static final String ACTIVITY_SIGNUP_ROUTING_KEY = "activity.signup.create";

    private final RabbitTemplate rabbitTemplate;

    public ActivitySignupRabbitConfig(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostConstruct
    public void initRabbitTemplateCallbacks() {
        rabbitTemplate.setConfirmCallback((CorrelationData correlationData, boolean ack, String cause) -> {
            if (ack) {
                return;
            }
            String id = correlationData == null ? null : correlationData.getId();
            log.error("Nack: correlationId={}, cause={}", id, cause);
        });

        rabbitTemplate.setReturnsCallback((ReturnedMessage returned) -> log.error(
                "Rabbit Return: replyCode={}, replyText={}, exchange={}, routingKey={}, correlationId={}, payload={}",
                returned.getReplyCode(),
                returned.getReplyText(),
                returned.getExchange(),
                returned.getRoutingKey(),
                returned.getMessage() == null ? null : returned.getMessage().getMessageProperties().getCorrelationId(),
                returned.getMessage() == null ? null : new String(returned.getMessage().getBody(), StandardCharsets.UTF_8)
        ));
    }

    @Bean
    public DirectExchange activitySignupExchange() {
        return new DirectExchange(ACTIVITY_SIGNUP_EXCHANGE, true, false);
    }

    @Bean
    public Queue activitySignupQueue() {
        return QueueBuilder.durable(ACTIVITY_SIGNUP_QUEUE).build();
    }

    @Bean
    public Binding activitySignupBinding(Queue activitySignupQueue, DirectExchange activitySignupExchange) {
        return BindingBuilder.bind(activitySignupQueue).to(activitySignupExchange).with(ACTIVITY_SIGNUP_ROUTING_KEY);
    }
}
