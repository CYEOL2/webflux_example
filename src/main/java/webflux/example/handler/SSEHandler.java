package webflux.example.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webflux.example.model.Notification;
import webflux.example.service.NotificationService;

@Slf4j
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
                    )
                    .doOnCancel(() -> log.info("doOnCancel 호출 되었습니다 userId: {}", userId))
                    .doFinally(signalType -> {
                        log.info("사용자 {}의 SSE 연결 최종 종료 (doFinally): {}", userId, signalType);
                        // signalType: CANCEL, COMPLETE, ON_ERROR 중 하나
                        // CANCEL -> log.info("클라이언트 종료로 인한 연결 종료");
                        // ON_ERROR -> log.info("오류로 인한 연결 종료");
                        // ON_COMPLETE -> log.info("정상 완료로 인한 연결 종료");
                    })
                    ;
            return ServerResponse.ok()
                    .contentType(MediaType.TEXT_EVENT_STREAM)
                    .body(eventStream, new ParameterizedTypeReference<ServerSentEvent<Notification>>(){})
                    .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue("처리중 오류가 발생하였습니다. [" + e.getMessage() + "]"));
        }catch (NumberFormatException e) {
            return ServerResponse.badRequest().bodyValue("잘못된 userId 형식입니다.");
        }

    }

    public Mono<ServerResponse> disconnect(ServerRequest request) {
        try {
            int userId = Integer.parseInt(request.pathVariable("userId"));
            return notificationService.disconnect(userId)
                    .then(ServerResponse.ok().build())
                    .doOnSuccess(v -> log.info("사용자 {}의 SSE 연결 종료 완료", userId))
                    .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue("처리중 오류가 발생하였습니다. [" + e.getMessage() + "]"));
        }catch (NumberFormatException e) {
            return ServerResponse.badRequest().bodyValue("잘못된 userId 형식입니다.");
        }
    }
}
