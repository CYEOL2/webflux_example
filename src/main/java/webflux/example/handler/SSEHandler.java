package webflux.example.handler;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webflux.example.model.Notification;
import webflux.example.service.NotificationService;

@Component
public class SSEHandler {

    private final NotificationService notificationService;

    public SSEHandler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public Mono<ServerResponse> stream(ServerRequest request) {
        try {
            int userId = Integer.parseInt(request.pathVariable("userId"));

            Flux<ServerSentEvent<Notification>> eventStream = notificationService.getNotificationStream(userId)
                    .map(notification -> ServerSentEvent.<Notification>builder()
                            .id(String.valueOf(notification.getUserId()) + System.currentTimeMillis()) //연결이 끊겼을 시 last-id 로 클라이언트에서 재연결함.
                            .event("alert-message")                                    //이벤트명 클라이언트에서 이 값으로 이벤트를 구분하여 데이터를 받음.
                            .data(notification)                                        //data
                            .build()
                    );
            return ServerResponse.ok()
                    .contentType(MediaType.TEXT_EVENT_STREAM)
                    .body(eventStream, new ParameterizedTypeReference<ServerSentEvent<Notification>>(){});
        }catch (NumberFormatException e) {
            return ServerResponse.badRequest().bodyValue("잘못된 userId 형식입니다.");
        }

    }
}
