package ru.dshirokov.reminder.telegramreminder.application;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.dshirokov.reminder.telegramreminder.application.dto.Alarm;
import ru.dshirokov.reminder.telegramreminder.application.dto.Identifier;

public interface ReminderService {
    Mono<MessageConvertible> startAddingEvent(Identifier identifier);
    Mono<MessageConvertible> startRemovingEvent(Identifier identifier);
    Mono<MessageConvertible> list(Identifier identifier);
    Mono<MessageConvertible> receive(Identifier identifier, String text);
    Flux<Alarm> alarms();
    Mono<MessageConvertible> help();
    Mono<MessageConvertible> startSetTimeZone(Identifier identifier);
    Mono<MessageConvertible> getTimeZone(Identifier identifier);
}
