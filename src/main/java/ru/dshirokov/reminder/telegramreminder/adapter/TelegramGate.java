package ru.dshirokov.reminder.telegramreminder.adapter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.dshirokov.reminder.telegramreminder.adapter.dto.Command;
import ru.dshirokov.reminder.telegramreminder.application.ReminderService;
import ru.dshirokov.reminder.telegramreminder.application.TextConvertible;
import ru.dshirokov.reminder.telegramreminder.application.dto.Identifier;

import java.util.function.Consumer;

@Component
@AllArgsConstructor
@Slf4j
public class TelegramGate extends TelegramLongPollingBot {

    private final ReminderService reminderService;

    private final TelegramProperties properties;

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()) {
            Message message = update.getMessage();

            Identifier identifier = new Identifier().setChatId(String.valueOf(message.getChatId()));

            if(message.isCommand()) {
                if(message.hasText()) {
                    switch (Command.from(message.getText())) {
                        case LIST:
                            reminderService.list(identifier)
                                    .map(TextConvertible::text).subscribe(respond(message.getChatId()), error());
                            break;
                        case ADD:
                            reminderService.startAddingEvent(identifier)
                                    .map(TextConvertible::text).subscribe(respond(message.getChatId()), error());
                            break;
                        case REMOVE:
                            reminderService.startRemovingEvent(identifier)
                                    .map(TextConvertible::text).subscribe(respond(message.getChatId()), error());
                            break;
                        case UNDEFINED:
                            reminderService.receive(identifier, message.getText())
                                    .map(TextConvertible::text).subscribe(respond(message.getChatId()), error());
                            break;
                    }
                }
            } else {
                reminderService.receive(identifier, message.getText())
                        .map(TextConvertible::text).subscribe(respond(message.getChatId()), error());
            }

        }
    }

    private Consumer<String> respond(Long chatId) {
        return text -> send(chatId, text);
    }

    private Consumer<Throwable> error() {
        return throwable -> log.error(throwable.getLocalizedMessage(), throwable);
    }

    public void send(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public String getBotUsername() {
        return properties.name;
    }

    @Override
    public String getBotToken() {
        return properties.token;
    }
}
