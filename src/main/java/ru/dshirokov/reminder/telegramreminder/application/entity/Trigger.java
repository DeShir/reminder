package ru.dshirokov.reminder.telegramreminder.application.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalTime;

@Data
@Accessors(chain = true)
public class Trigger {
    LocalTime time;
}
