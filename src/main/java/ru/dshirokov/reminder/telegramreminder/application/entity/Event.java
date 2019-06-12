package ru.dshirokov.reminder.telegramreminder.application.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class Event {
    UUID id;
    String chatId;
    Trigger trigger;
    String title;
    Boolean suspended;
}
