package ru.dshirokov.reminder.telegramreminder.application.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.dshirokov.reminder.telegramreminder.application.dto.State;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class Interaction {
    String id;
    State state;
    UUID eventId;
    String timeZone;
}
