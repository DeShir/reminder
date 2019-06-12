package ru.dshirokov.reminder.telegramreminder.application.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.dshirokov.reminder.telegramreminder.adapter.dto.Command;
import ru.dshirokov.reminder.telegramreminder.application.ReminderService;
import ru.dshirokov.reminder.telegramreminder.application.TextConvertible;
import ru.dshirokov.reminder.telegramreminder.application.dto.Alarm;
import ru.dshirokov.reminder.telegramreminder.application.dto.Identifier;
import ru.dshirokov.reminder.telegramreminder.application.dto.ListTextResponse;
import ru.dshirokov.reminder.telegramreminder.application.dto.SimpleTextResponse;
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
    public Mono<TextConvertible> startAddingEvent(Identifier identifier) {
        return interactionRepository
                .findByIdOrNew(identifier.getChatId())
                .filter(interaction -> PENDING.equals(interaction.getState()))
                .zipWith(eventRepository.create(identifier.getChatId()), (interaction, event) ->
                        interaction.setEventId(event.getId()).setState(ADD_EVENT_TITLE))
                .flatMap(interactionRepository::save).then(Mono.just(new SimpleTextResponse("Название?")));
    }

    @Override
    public Mono<TextConvertible> startRemovingEvent(Identifier identifier) {
        return interactionRepository
                .findByIdOrNew(identifier.getChatId())
                .doOnSuccess(interaction -> interaction.setState(REMOVE_EVENT))
                .flatMap(interactionRepository::save)
                .then(eventRepository
                        .findAllByChatId(identifier.getChatId())
                        .map(event -> String.format("%s \n %s", event.getTitle(), event.getId()))
                        .collect(Collectors.toList()).map(ListTextResponse::new));
    }

    @Override
    public Mono<TextConvertible> list(Identifier identifier) {
        return eventRepository
                .findAllByChatId(identifier.getChatId())
                .map(event -> String.format("%s - %s\n%s\n", event.getTrigger().getTime(), event.getTitle(), event.getId()))
                .collect(Collectors.toList()).map(ListTextResponse::new);
    }

    @Override
    public Mono<TextConvertible> receive(Identifier identifier, String text) {
        return interactionRepository
                .findByIdOrNew(identifier.getChatId())
                .flatMap(interaction -> {
                    switch (interaction.getState()) {
                        case ADD_EVENT_TITLE: return addEventTitle(interaction, text);
                        case ADD_EVENT_TRIGGER: return addEventTrigger(interaction, text);
                        case REMOVE_EVENT: return removeEvent(text);
                        case PENDING:
                        default: return Mono.just(new SimpleTextResponse(String.format("Создать событие: %s", Command.ADD.literal())));
                    }
                });
    }

    @Override
    public Flux<Alarm> alarms() {
        return eventRepository.findAllBySuspendedIsFalse()
                .filter(event -> shouldTrigger(event.getTrigger()))
                .map(event -> new Alarm().setChatId(event.getChatId()).setMessage(new SimpleTextResponse(event.getTitle())));
    }

    private Mono<TextConvertible> addEventTitle(Interaction interaction, String text) {
        return eventRepository
                .findById(interaction.getEventId())
                .doOnSuccess(event -> event.setTitle(text))
                .flatMap(eventRepository::save)
                .then(interactionRepository.save(interaction.setState(ADD_EVENT_TRIGGER)))
                .then(Mono.just(new SimpleTextResponse("Время?")));
    }

    private Mono<TextConvertible> addEventTrigger(Interaction interaction, String text) {
        return eventRepository
                .findById(interaction.getEventId())
                .doOnSuccess(event -> event.setTrigger(from(text)).setSuspended(Boolean.FALSE))
                .flatMap(eventRepository::save)
                .then(interactionRepository.save(interaction.setState(PENDING)))
                .then(Mono.just(new SimpleTextResponse("Спасибо, событие создано!")));
    }

    private Mono<TextConvertible> removeEvent(String text) {
        return eventRepository
                .findById(UUID.fromString(text))
                .flatMap(eventRepository::delete)
                .then(Mono.just(new SimpleTextResponse("Событие удалено!")));
    }
}
