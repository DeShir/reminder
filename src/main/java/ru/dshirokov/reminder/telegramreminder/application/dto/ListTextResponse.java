package ru.dshirokov.reminder.telegramreminder.application.dto;

import lombok.AllArgsConstructor;
import ru.dshirokov.reminder.telegramreminder.application.MessageConvertible;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class ListTextResponse implements MessageConvertible {

    private final List<String> items;

    @Override
    public Optional<String> text() {
        return Optional.of(String.join("\n", items));
    }
}
