package ru.dshirokov.reminder.telegramreminder.application;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.dshirokov.reminder.telegramreminder.application.entity.Trigger;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;

@UtilityClass
@Slf4j
public class Triggers {

    private final static long PADDING = 30;

    public Trigger from(String description) {
        return new Trigger().setTime(OffsetTime.parse(description).withOffsetSameInstant(OffsetDateTime.now().getOffset()).toLocalTime());
    }

    public Trigger from(String description, String offset) {
        return from(String.format("%s%s", description, offset));
    }

    public boolean shouldTrigger(Trigger trigger) {
        LocalTime now = LocalTime.now();
        return now.minusSeconds(PADDING).isBefore(trigger.getTime()) && now.plusSeconds(PADDING).isAfter(trigger.getTime());
    }
}
