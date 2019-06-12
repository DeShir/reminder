package ru.dshirokov.reminder.telegramreminder.application.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Identifier {
    String chatId;
}
