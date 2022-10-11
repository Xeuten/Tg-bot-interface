package com.example.telegraminterface.rabbitmq;

import com.example.telegraminterface.controllers.TelegramBotUpdateController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQMessageHandler {

    @Autowired
    private TelegramBotUpdateController telegramBotUpdateController;

    private final String successGET = "Get request has been processed.\n";
    private final String successDELETE = "Delete request has been processed\n";
    private final String deleted = "Item has been deleted.";
    private final String sucessUPDATES = "Updates request has been processed.\n";


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
            return deleted;
        }
    }

}
