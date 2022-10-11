package com.example.telegraminterface.model;

import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name="updates")
@Data
public class TelegramUpdate {

    public TelegramUpdate() {}

    public TelegramUpdate(Update update) {
        this.update_id = update.getUpdateId();
        this.username = update.getMessage().getFrom().getUserName();
        this.message_timestamp = OffsetDateTime.of(LocalDateTime
                .ofEpochSecond(update.getMessage().getDate(), 0, ZoneOffset.UTC), ZoneOffset.UTC);
        this.message_text = update.getMessage().getText();
        this.chat_id = update.getMessage().getChatId();
    }

    @Id
    @Column
    private Integer update_id;

    @Column
    private String username;

    @Column
    private OffsetDateTime message_timestamp;

    @Column
    private String message_text;

    @Column
    private Long chat_id;

}
