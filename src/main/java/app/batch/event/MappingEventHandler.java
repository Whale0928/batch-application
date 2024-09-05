package app.batch.event;

import app.batch.domain.Mapping;
import app.batch.repository.JpaMappingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class MappingEventHandler {
    private final JpaMappingRepository mappingRepository;

    @Transactional
    @EventListener
    public void handleMappingEvent(Mapping event) {
        mappingRepository.save(event);
    }
}
