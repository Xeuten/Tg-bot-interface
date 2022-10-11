package com.example.telegraminterface.rabbitmq;

import com.example.telegraminterface.controllers.TelegramBotUpdateController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQMessageHandler {

    @Autowired
    private TelegramBotUpdateController telegramBotUpdateController;

    private final String successGET = "Запрос на получение обработан.\n";
    private final String successDELETE = "Запрос на удаление обработан.\n";
    private final String sucessUPDATES = "Запрос списка обновлений обработан.\n";


    public void sendAnswer(String backendResponse) {
        String parsedMessage = toJsonFormat(customTrim(backendResponse.split("\n")[2]));
        Long id = Long.parseLong(backendResponse.split("\n")[0]);
        switch (backendResponse.split("\n")[1]) {
         case "GET" -> telegramBotUpdateController.sendMessage(id, successGET + parsedMessage);
         case "DELETE" -> telegramBotUpdateController.sendMessage(id, successDELETE + parsedMessage);
         case "UPDATES" -> telegramBotUpdateController.sendMessage(id, sucessUPDATES + parsedMessage);
        }
    }

    private String toJsonFormat(String jsonString) {
        return jsonString.replace(", ", ",\n\t")
                .replace("{", "{\n\t").replace("}","\n\t}")
                .replace("[", "[\n\t").replace("]","\n\t]")
                .replace("=", ": ");
    }

    private String customTrim(String message) {
        try {
            return message.substring(message.indexOf("{"), message.lastIndexOf("}") + 1);
        } catch (Exception e) {
            return "";
        }
    }

}
