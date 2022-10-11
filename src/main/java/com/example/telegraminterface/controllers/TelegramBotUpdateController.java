package com.example.telegraminterface.controllers;
import com.example.telegraminterface.botconfig.BotConfig;
import com.example.telegraminterface.service.UpdateService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;


@Component
public class TelegramBotUpdateController extends TelegramLongPollingBot {

    @Autowired
    private UpdateService updateService;

    final BotConfig config;

    public TelegramBotUpdateController(BotConfig config) { this.config = config; }

    @Override
    public String getBotUsername() { return config.getBotName(); }

    @Override
    public String getBotToken() { return config.getToken(); }

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        Pair<String, Optional<HttpRequest>> messageAndRequest = updateService.processUpdate(update);
        sendMessage(update.getMessage().getChatId(), messageAndRequest.getFirst());
        if (messageAndRequest.getSecond().isPresent()) {
            HttpClient client = HttpClient.newHttpClient();
            client.send(messageAndRequest.getSecond().get(), HttpResponse.BodyHandlers.ofString());
        }
    }

    public void sendMessage(long chatid, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatid);
        message.setText(textToSend);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
