package ru.dshirokov.reminder.telegramreminder.application.dto;

import lombok.AllArgsConstructor;
import ru.dshirokov.reminder.telegramreminder.application.TextConvertible;

@AllArgsConstructor
public class SimpleTextResponse implements TextConvertible {

    private final String message;

    @Override
    public String text() {
        return message;
    }
}
