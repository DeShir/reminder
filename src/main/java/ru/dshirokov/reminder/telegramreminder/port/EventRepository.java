package ru.dshirokov.reminder.telegramreminder.port;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.dshirokov.reminder.telegramreminder.application.entity.Event;

import java.util.UUID;

public interface EventRepository extends ReactiveCrudRepository<Event, UUID> {

    default Mono<Event> create(String chatId) {
        return save(new Event().setChatId(chatId).setId(UUID.randomUUID()).setSuspended(Boolean.TRUE));
    }

    Flux<Event> findAllByChatId(String chatId);

    Flux<Event> findAllBySuspendedIsFalse();
}
