package ru.dshirokov.reminder.telegramreminder.application.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.dshirokov.reminder.telegramreminder.application.MessageConvertible;

@Data
@Accessors(chain = true)
public class Alarm {
    String chatId;
    MessageConvertible message;
}
