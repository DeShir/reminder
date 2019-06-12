package ru.dshirokov.reminder.telegramreminder.application;

import lombok.experimental.UtilityClass;
import ru.dshirokov.reminder.telegramreminder.application.entity.Trigger;

import java.time.LocalTime;
import java.time.OffsetTime;
import java.util.Date;

@UtilityClass
public class Triggers {

    private final static long PADDING = 30;

    public Trigger from(String description) {
        return new Trigger().setTime(LocalTime.parse(description));
    }
    public boolean shouldTrigger(Trigger trigger) {
        LocalTime now = LocalTime.now();
        return now.minusSeconds(PADDING).isBefore(trigger.getTime()) && now.plusSeconds(PADDING).isAfter(trigger.getTime());
    }
}
