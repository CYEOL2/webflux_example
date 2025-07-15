package webflux.example.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import webflux.example.mange.NotificationManager;

@Service
public class NotificationService {

    private final NotificationManager sinkManager;

    public NotificationService(NotificationManager sinkManager) {
        this.sinkManager = sinkManager;
    }

    // 유저별 sink
    public Flux<webflux.example.model.Notification> getNotificationStream(int userId){
        return sinkManager.createSinks(userId).asFlux();
    }

}
