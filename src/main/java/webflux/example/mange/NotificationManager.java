package webflux.example.mange;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import webflux.example.model.Notification;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class NotificationManager {
    // 유저ID별 Sinks Map
    private final Map<Integer, Sinks.Many<Notification>> sinksMap = new ConcurrentHashMap<>();

    // SSE 연결 요청 들어왔을때 CREATE
    public Sinks.Many<Notification> createSinks(int userId){
        log.info("creating sink for user: {}", userId);
        Sinks.Many<Notification> sinks = Sinks.many()
                .multicast()
                .onBackpressureBuffer();
        sinksMap.put(userId, sinks);
        return sinks;
    }
    // 알림 PUSH할때 GET
    public Sinks.Many<Notification> getSinks(int userId){
        return sinksMap.get(userId);
    }
    // 연결 종료시 (ex 로그아웃시 호출)
    public Mono<Void> removeSinks(int userId) {
        sinksMap.remove(userId);
        return Mono.empty();
    }
}
