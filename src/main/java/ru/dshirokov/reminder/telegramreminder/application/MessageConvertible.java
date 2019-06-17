package ru.dshirokov.reminder.telegramreminder.application;

import java.util.List;
import java.util.Optional;

public interface MessageConvertible {
    default Optional<String> text() {
        return Optional.empty();
    }
    default Optional<List<List<String>>> keyboard() {
        return Optional.empty();
    }
}
