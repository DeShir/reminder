package ru.dshirokov.reminder.telegramreminder.application.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.dshirokov.reminder.telegramreminder.application.ReminderService;
import ru.dshirokov.reminder.telegramreminder.application.MessageConvertible;
import ru.dshirokov.reminder.telegramreminder.application.dto.*;
import ru.dshirokov.reminder.telegramreminder.application.entity.Interaction;
import ru.dshirokov.reminder.telegramreminder.port.EventRepository;
import ru.dshirokov.reminder.telegramreminder.port.InteractionRepository;

import java.util.UUID;
import java.util.stream.Collectors;

import static ru.dshirokov.reminder.telegramreminder.application.Triggers.*;
import static ru.dshirokov.reminder.telegramreminder.application.dto.State.*;

@Service
@AllArgsConstructor
public class ReminderServiceImpl implements ReminderService {

    private final EventRepository eventRepository;

    private final InteractionRepository interactionRepository;

    @Override
    @Transactional
    public Mono<MessageConvertible> startAddingEvent(Identifier identifier) {
        return interactionRepository
                .findByIdOrNew(identifier.getChatId())
                .filter(interaction -> PENDING.equals(interaction.getState()))
                .zipWith(eventRepository.create(identifier.getChatId()), (interaction, event) ->
                        interaction.setEventId(event.getId()).setState(ADD_EVENT_TITLE))
                .flatMap(interactionRepository::save).then(Mono.just(new SimpleTextResponse("Название?")));
    }

    @Override
    public Mono<MessageConvertible> startRemovingEvent(Identifier identifier) {
        return interactionRepository
                .findByIdOrNew(identifier.getChatId())
                .doOnSuccess(interaction -> interaction.setState(REMOVE_EVENT))
                .flatMap(interactionRepository::save)
                .then(eventRepository
                        .findAllByChatId(identifier.getChatId())
                        .map(event -> String.format("%s \n %s", event.getTitle(), event.getId()))
                        .collect(Collectors.toList()).doOnSuccess(list -> list.add("Идентификатор события?")).map(ListTextResponse::new));
    }

    @Override
    public Mono<MessageConvertible> list(Identifier identifier) {
        return eventRepository
                .findAllByChatId(identifier.getChatId())
                .map(event -> String.format("%s - %s\n%s\n", event.getTrigger().getTime(), event.getTitle(), event.getId()))
                .collect(Collectors.toList()).map(ListTextResponse::new);
    }

    @Override
    public Mono<MessageConvertible> receive(Identifier identifier, String text) {
        return interactionRepository
                .findByIdOrNew(identifier.getChatId())
                .flatMap(interaction -> {
                    switch (interaction.getState()) {
                        case ADD_EVENT_TITLE: return addEventTitle(interaction, text);
                        case ADD_EVENT_TRIGGER: return addEventTrigger(interaction, text);
                        case REMOVE_EVENT: return removeEvent(text);
                        case START_SETTING_TIMEZONE: return setTimeZone(interaction, text);
                        case PENDING:
                        default: return Mono.empty();
                    }
                });
    }

    @Override
    public Flux<Alarm> alarms() {
        return eventRepository.findAllBySuspendedIsFalse()
                .filter(event -> shouldTrigger(event.getTrigger()))
                .map(event -> new Alarm().setChatId(event.getChatId()).setMessage(new SimpleTextResponse(event.getTitle())));
    }

    @Override
    public Mono<MessageConvertible> help() {
        return Mono.just(new SimpleTextResponse("Создать событие /add\nСписок событий /list\nУдалить событие /remove"));
    }

    @Override
    public Mono<MessageConvertible> startSetTimeZone(Identifier identifier) {
        return interactionRepository
                .findByIdOrNew(identifier.getChatId())
                .doOnSuccess(interaction -> interaction.setState(START_SETTING_TIMEZONE))
                .flatMap(interactionRepository::save)
                .then(Mono.just(new SimpleKeyboardResponse("timezones", "Часовой пояс?")));
    }

    @Override
    public Mono<MessageConvertible> getTimeZone(Identifier identifier) {
        return interactionRepository
                .findByIdOrNew(identifier.getChatId())
                .map(interaction -> new SimpleTextResponse(interaction.getTimeZone()));
    }

    private Mono<MessageConvertible> setTimeZone(Interaction interaction, String text) {
        return interactionRepository.save(interaction.setTimeZone(text).setState(PENDING))
                .then(Mono.just(new SimpleTextResponse("Временная зона установлена.")));
    }

    private Mono<MessageConvertible> addEventTitle(Interaction interaction, String text) {
        return eventRepository
                .findById(interaction.getEventId())
                .doOnSuccess(event -> event.setTitle(text))
                .flatMap(eventRepository::save)
                .then(interactionRepository.save(interaction.setState(ADD_EVENT_TRIGGER)))
                .then(Mono.just(new SimpleTextResponse("Время?")));
    }

    private Mono<MessageConvertible> addEventTrigger(Interaction interaction, String text) {
        return eventRepository
                .findById(interaction.getEventId())
                .doOnSuccess(event -> event.setTrigger(from(text, interaction.getTimeZone())).setSuspended(Boolean.FALSE))
                .flatMap(eventRepository::save)
                .then(interactionRepository.save(interaction.setState(PENDING)))
                .then(Mono.just(new SimpleTextResponse("Спасибо, событие создано!")));
    }

    private Mono<MessageConvertible> removeEvent(String text) {
        return eventRepository
                .findById(UUID.fromString(text))
                .flatMap(eventRepository::delete)
                .then(Mono.just(new SimpleTextResponse("Событие удалено!")));
    }
}
