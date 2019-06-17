package ru.dshirokov.reminder.telegramreminder.application.dto;

import lombok.AllArgsConstructor;
import ru.dshirokov.reminder.telegramreminder.application.MessageConvertible;

import java.util.Optional;

@AllArgsConstructor
public class SimpleTextResponse implements MessageConvertible {

    private final String message;

    @Override
    public Optional<String> text() {
        return Optional.of(message);
    }
}
