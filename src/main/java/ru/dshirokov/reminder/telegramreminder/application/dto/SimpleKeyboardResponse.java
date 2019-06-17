package ru.dshirokov.reminder.telegramreminder.application.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import ru.dshirokov.reminder.telegramreminder.application.MessageConvertible;
import ru.dshirokov.reminder.telegramreminder.application.Resources;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class SimpleKeyboardResponse implements MessageConvertible {

    private final String key;

    private final String text;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Optional<String> text() {
        return Optional.of(text);
    }

    @Override
    @SneakyThrows
    public Optional<List<List<String>>> keyboard() {
        return Optional.of(objectMapper.readValue(Resources.read(String.format("/template/keyboard/%s.ftl", key)), new TypeReference<List<List<String>>>() {}));
    }
}
