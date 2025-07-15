package webflux.example.handler;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import webflux.example.model.Order;
import webflux.example.model.User;
import webflux.example.service.ApiService;

@Component
public class ApiHandler {

    private final ApiService apiService;

    public ApiHandler(ApiService apiService) {
        this.apiService = apiService;
    }
    // 유저 생성
    public Mono<ServerResponse> createUser(ServerRequest request) {
        return request.bodyToMono(User.class)
                .switchIfEmpty(Mono.error(new Exception("User is null")))
                .flatMap(user -> apiService.createUser(user))
                .flatMap(user -> ServerResponse.ok().bodyValue(user))
                .onErrorResume(e ->
                        ServerResponse.badRequest().bodyValue("처리중 오류가 발생하였습니다. [" + e.getMessage() + "]")
                );
    }
    // 유저 전체 조회
    public Mono<ServerResponse> getAllUsers(ServerRequest request) {
        return apiService.getAllUsers()
                .collectList()
                .flatMap(users -> ServerResponse.ok().bodyValue(users))
                .onErrorResume(e ->
                        ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue("처리중 오류가 발생하였습니다. [" + e.getMessage() + "]")
                );
    }
    // 유저 조회
    public Mono<ServerResponse> getUser(ServerRequest request) {
        try {
            int userId = Integer.parseInt(request.pathVariable("userId"));
            return apiService.getUser(userId)
                    .flatMap(user -> ServerResponse.ok().bodyValue(user))
                    .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND).build())
                    .onErrorResume(e ->
                            ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue("처리중 오류가 발생하였습니다. [" + e.getMessage() + "]")
                    );
        }catch (NumberFormatException e) {
            return ServerResponse.badRequest().bodyValue("잘못된 userId 형식입니다.");
        }
    }

    public Mono<ServerResponse> updateUser(ServerRequest request) {
        try {
            int userId = Integer.parseInt(request.pathVariable("userId"));
            return request.bodyToMono(User.class)
                    .switchIfEmpty(Mono.error(new Exception("User is null")))
                    .flatMap(user -> apiService.updateUser(userId, user))
                    .flatMap(user -> ServerResponse.ok().bodyValue(user))
                    .onErrorResume(e ->
                            ServerResponse.badRequest().bodyValue("처리중 오류가 발생하였습니다. [" + e.getMessage() + "]")
                    );
        }catch (NumberFormatException e) {
            return ServerResponse.badRequest().bodyValue("잘못된 userId 형식입니다.");
        }
    }
    public Mono<ServerResponse> deleteUser(ServerRequest request){
        try {
            int userId = Integer.parseInt(request.pathVariable("userId"));
            return apiService.deleteUser(userId)
                    .flatMap(success -> {
                        if(success) {
                            return ServerResponse.ok().build();
                        }else{
                            return Mono.error(new Exception("User not found"));
                        }
                    })
                    .onErrorResume(e -> ServerResponse.badRequest().bodyValue("처리중 오류가 발생하였습니다. [" + e.getMessage() + "]"));
        } catch (NumberFormatException e) {
            return ServerResponse.badRequest().bodyValue("잘못된 userId 형식입니다.");
        }
    }

    // 주문 생성
    public Mono<ServerResponse> createOrder(ServerRequest request) {
        return request.bodyToMono(Order.class)
                .switchIfEmpty(Mono.error(new Exception("Order is null")))
                .flatMap(order -> apiService.createOrder(order))
                .flatMap(order -> ServerResponse.ok().bodyValue(order))
                .onErrorResume(e ->
                        ServerResponse.badRequest().bodyValue("처리중 오류가 발생하였습니다. [" + e.getMessage() + "]")
                );
    }
    // 주문 상세조회(유저 정보, 상품정보 포함)
    public Mono<ServerResponse> getOrderDetails(ServerRequest request) {
        try {
            int orderId = Integer.parseInt(request.pathVariable("orderId"));
            return apiService.getOrderDetails(orderId)
                    .flatMap(orderDetails -> ServerResponse.ok().bodyValue(orderDetails))
                    .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND).build())
                    .onErrorResume(e -> ServerResponse.badRequest().bodyValue("처리중 오류가 발생하였습니다. [" + e.getMessage() + "]"));
        } catch (NumberFormatException e) {
            return ServerResponse.badRequest().bodyValue("잘못된 orderId 형식입니다.");
        }
    }

    // 주문 상태 변경
    public Mono<ServerResponse> updateOrder(ServerRequest request) {
        try {
            int orderId = Integer.parseInt(request.pathVariable("orderId"));
            return request.bodyToMono(Order.class)
                    .switchIfEmpty(Mono.error(new Exception("Order is null")))
                    .flatMap(order -> apiService.updateOrder(orderId, order))
                    .flatMap(order -> ServerResponse.ok().bodyValue(order))
                    .onErrorResume(e -> ServerResponse.badRequest().bodyValue("처리중 오류가 발생하였습니다. [" + e.getMessage() + "]"));
        } catch (NumberFormatException e) {
            return ServerResponse.badRequest().bodyValue("잘못된 orderId 형식입니다.");
        }
    }
}
