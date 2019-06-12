package ru.dshirokov.reminder.telegramreminder.adapter;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.dshirokov.reminder.telegramreminder.application.ReminderService;

@Service
@AllArgsConstructor
public class Ticker {

    private final ReminderService reminderService;

    private final TelegramGate telegramGate;

    @Scheduled(cron = "0 * * * * *")
    public void tick() {
        reminderService.alarms().subscribe(alarm -> telegramGate.send(Long.valueOf(alarm.getChatId()), alarm.getMessage().text()));
    }
}
