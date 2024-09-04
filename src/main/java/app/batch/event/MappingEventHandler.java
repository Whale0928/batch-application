package app.batch.event;

import app.batch.domain.Mapping;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;


@Component
@RequiredArgsConstructor
public class MappingEventHandler {

    private final EntityManager em;

    @TransactionalEventListener
    public void handleMappingEvent(Mapping event) {
        em.persist(event);
    }
}
