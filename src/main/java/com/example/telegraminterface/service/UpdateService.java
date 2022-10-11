package com.example.telegraminterface.service;

import com.example.telegraminterface.model.TelegramUpdate;
import com.example.telegraminterface.persistence.UpdatesRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.Optional;

@Component
public class UpdateService {

    @Autowired
    private UpdatesRepository updatesRepository;

    @Value("${request.get}")
    private String GETrequestUrl;

    @Value("${request.delete}")
    private String DELETErequestUrl;

    @Value("${request.updates}")
    private String UPDATESrequestUrl;

    private final String commandList = "Command list:\n\"/findItem {id}\" - get information about an element by id\n\"/deleteItem {id}\" - delete an item by id\n\"/updatesFrom {date}\" - get a list of files updated 24 hours before the given date";
    private final String invalidFormat = "Incorrect command format.";
    private final String getSent = "Get request has been sent.";
    private final String deleteSent = "Delete request has been sent.";
    private final String updatesSent = "Updates request has been sent.";
    private final String serverUnavailable = "Server unavailable.";

    @SneakyThrows
    public Pair<String, Optional<HttpRequest>> processUpdate(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            updatesRepository.save(new TelegramUpdate(update));
            return executeCommand(parseMessageToCommand(update.getMessage().getText()), update.getMessage().getChatId());
        } else {
            return Pair.of(invalidFormat, Optional.empty());
        }
    }

    @SneakyThrows
    private HttpRequest buildHttpRequest(String URL, String requestType, String requestBody, Long chatId) {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(new URI(URL))
                .headers("Content-Type", "text/plain;charset=UTF-8", HttpHeaders.USER_AGENT, "tgBotBackend/" + chatId);
        HttpRequest request = switch (requestType) {
            case "GET" -> requestBuilder.GET().build();
            case "DELETE" -> requestBuilder.DELETE().build();
            case "UPDATES" -> requestBuilder.POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();
            default -> HttpRequest.newBuilder().build();
        };
        return request;
    }

    private Pair<String, Optional<String>> parseMessageToCommand(String message) {
        String[] messageHalves = message.split(" ", 2);
        return Pair.of(messageHalves[0], messageHalves.length > 1 ? Optional.of(messageHalves[1]) : Optional.empty());
    }

    private Pair<String, Optional<HttpRequest>> executeCommand(Pair<String, Optional<String>> commandAndArg, Long chatId) {
        HttpRequest request;
        if(commandAndArg.getFirst().equals("/start") && commandAndArg.getSecond().isEmpty()) {
            return Pair.of(commandList, Optional.empty());
        }
        if(commandAndArg.getSecond().isPresent()) {
            String arg = commandAndArg.getSecond().get().replace(" ", "%20");
            try {
                switch(commandAndArg.getFirst()) {
                    case "/findItem":
                        request = buildHttpRequest(GETrequestUrl + arg, "GET", "", chatId);
                        return Pair.of(getSent, Optional.of(request));
                    case "/deleteItem":
                        request = buildHttpRequest(DELETErequestUrl + arg, "DELETE", "", chatId);
                        return Pair.of(deleteSent, Optional.of(request));
                    case "/updatesFrom":
                        request = buildHttpRequest(UPDATESrequestUrl, "UPDATES", arg, chatId);
                        return Pair.of(updatesSent, Optional.of(request));
                    default:
                        return Pair.of(invalidFormat, Optional.empty());
                }
            } catch (Exception e) {
                return Pair.of(serverUnavailable, Optional.empty());
            }
        }
        return Pair.of(invalidFormat, Optional.empty());
    }

}
