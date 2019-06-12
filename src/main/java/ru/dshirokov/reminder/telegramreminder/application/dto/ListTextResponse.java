package ru.dshirokov.reminder.telegramreminder.application.dto;

import lombok.AllArgsConstructor;
import ru.dshirokov.reminder.telegramreminder.application.TextConvertible;

import java.util.List;

@AllArgsConstructor
public class ListTextResponse implements TextConvertible {

    private final List<String> items;

    @Override
    public String text() {
        return String.join("\n", items);
    }
}
