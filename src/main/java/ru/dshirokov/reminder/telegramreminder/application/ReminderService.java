package ru.dshirokov.reminder.telegramreminder.application;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.dshirokov.reminder.telegramreminder.application.dto.Alarm;
import ru.dshirokov.reminder.telegramreminder.application.dto.Identifier;

public interface ReminderService {
    Mono<TextConvertible> startAddingEvent(Identifier identifier);
    Mono<TextConvertible> startRemovingEvent(Identifier identifier);
    Mono<TextConvertible> list(Identifier identifier);
    Mono<TextConvertible> receive(Identifier identifier, String text);
    Flux<Alarm> alarms();
    Mono<TextConvertible> help();
}
