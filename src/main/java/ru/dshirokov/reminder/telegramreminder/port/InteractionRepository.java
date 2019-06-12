package ru.dshirokov.reminder.telegramreminder.port;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.dshirokov.reminder.telegramreminder.application.entity.Interaction;

import static ru.dshirokov.reminder.telegramreminder.application.dto.State.PENDING;

public interface InteractionRepository extends ReactiveCrudRepository<Interaction, String> {
    default Mono<Interaction> findByIdOrNew(String id) {
        return findById(id).defaultIfEmpty(new Interaction().setState(PENDING).setId(id));
    }
}
