package webflux.example.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class Notification {

    private int userId;
    private String title;
    private String message;
    private LocalDateTime createDtime;

}
