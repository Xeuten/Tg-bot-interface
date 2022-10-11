package com.example.telegraminterface.persistence;

import com.example.telegraminterface.model.TelegramUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UpdatesRepository extends JpaRepository<TelegramUpdate, String> {
}
