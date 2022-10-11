package com.example.telegraminterface.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "rabbitmq.queue", id = "listener")
public class RabbitMQReceiver {

    @Autowired
    private RabbitMQMessageHandler handler;

    @RabbitHandler
    public void receiveMessage(String backendResponse) {
        handler.sendAnswer(backendResponse);
    }

}
