package webflux.example.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import webflux.example.mange.NotificationManager;
import webflux.example.model.Notification;

@Service
public class NotificationService {

    private final NotificationManager sinkManager;

    public NotificationService(NotificationManager sinkManager) {
        this.sinkManager = sinkManager;
    }

    // 유저별 sink
    public Flux<Notification> getNotificationStream(int userId){
        return sinkManager.createSinks(userId).asFlux();
    }
    // SSE 연결 종료
    public Mono<Void> disconnect(int userId){
        Sinks.EmitResult emitResult = sinkManager.getSinks(userId).tryEmitComplete();
        if(emitResult.isSuccess()){
            //sinkManager.getSinks(userId).tryEmitError(new ResponseStatusException(HttpStatus.NOT_FOUND));
            sinkManager.removeSinks(userId);
            return Mono.empty();
        }else{
            return Mono.error(new Exception("SSE 커넥션 종료시 오류가 발생하였습니다."));
        }
    }


}
