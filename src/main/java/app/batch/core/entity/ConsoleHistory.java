package app.batch.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ConsoleHistory {
    @Id
    private UUID consoleId;
    private String var1;
    private String var2;
    private LocalDateTime createdAt;

    public static ConsoleHistory of(String var1, String var2) {
        return ConsoleHistory.builder()
                .consoleId(UUID.randomUUID())
                .var1(var1)
                .var2(var2)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
