package ru.dshirokov.reminder.telegramreminder.application.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.dshirokov.reminder.telegramreminder.application.TextConvertible;

@Data
@Accessors(chain = true)
public class Alarm {
    String chatId;
    TextConvertible message;
}
