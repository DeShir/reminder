package ru.dshirokov.reminder.telegramreminder.application;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@UtilityClass
public class Resources {

    @SneakyThrows
    public String read(String path) {
        try(InputStream inputStream = Resources.class.getResourceAsStream(path)) {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }

            return result.toString(StandardCharsets.UTF_8.name());
        }
    }
}
