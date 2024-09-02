package app.batch.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Entity(name = "console_history")
public class ConsoleHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long consoleId;
    private String var1;
    private String var2;
    private LocalDateTime createdAt;

    public static ConsoleHistory of(String var1, String var2) {
        return ConsoleHistory.builder()
                .var1(var1)
                .var2(var2)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
