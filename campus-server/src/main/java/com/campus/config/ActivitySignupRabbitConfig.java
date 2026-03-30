package com.campus.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActivitySignupRabbitConfig {

    public static final String ACTIVITY_SIGNUP_EXCHANGE = "activity.signup.direct";
    public static final String ACTIVITY_SIGNUP_QUEUE = "activity.signup.queue";
    public static final String ACTIVITY_SIGNUP_ROUTING_KEY = "activity.signup.create";

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

