package ru.dshirokov.reminder.telegramreminder.adapter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "telegram")
@Data
public class TelegramProperties {
    String token;
    String name;
}
