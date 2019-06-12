package ru.dshirokov.reminder.telegramreminder.application;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.dshirokov.reminder.telegramreminder.application.entity.Trigger;

import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;

@UtilityClass
@Slf4j
public class Triggers {

    private final static long PADDING = 30;

    public Trigger from(String description) {
        return new Trigger().setTime(OffsetTime.parse(description).withOffsetSameInstant(ZoneOffset.UTC).toLocalTime());
    }
    public boolean shouldTrigger(Trigger trigger) {
        LocalTime now = LocalTime.now();
        return now.minusSeconds(PADDING).isBefore(trigger.getTime()) && now.plusSeconds(PADDING).isAfter(trigger.getTime());
    }
}
