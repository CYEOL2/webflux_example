package webflux.example.service;

import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import webflux.example.mange.NotificationManager;
import webflux.example.model.*;
import webflux.example.repository.OrdersRepository;
import webflux.example.repository.ProductsRepository;
import webflux.example.repository.UsersRepository;

import java.time.LocalDateTime;

@Service
public class ApiService {

    private final UsersRepository usersRepository;
    private final OrdersRepository ordersRepository;
    private final ProductsRepository productsRepository;
    private final NotificationManager notificationManager;

    public ApiService(UsersRepository usersRepository, OrdersRepository ordersRepository, ProductsRepository productsRepository, NotificationManager notificationManager) {
        this.usersRepository = usersRepository;
        this.ordersRepository = ordersRepository;
        this.productsRepository = productsRepository;
        this.notificationManager = notificationManager;
    }
    // 유저 생성
    public Mono<User> createUser(User user) {
        return usersRepository.save(user);
    }
    // 유저 전체 조회
    public Flux<User> getAllUsers() {
        return usersRepository.findAll();
    }
    // 유저 조회
    public Mono<User> getUser(int userId) {
        return usersRepository.findById(userId);
    }

    //유저 업데이트 ( insert와 update 모두 save 메서드 사용한다.)
    public Mono<User> updateUser(int userId, User user) {
        return usersRepository.findById(userId)
                .map(existingUser -> User.builder()
                        .id(existingUser.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .createdAt(existingUser.getCreatedAt())
                        .build())
                .switchIfEmpty(Mono.error(new Exception("User not found")))
                .flatMap(usersRepository::save);
    }

    //유저 삭제
    public Mono<Boolean> deleteUser(int userId) {
        return usersRepository.existsById(userId)
                .flatMap(exists -> {
                   if(exists) {
                       return usersRepository.deleteById(userId).thenReturn(true);
                   }else{
                       return Mono.just(false);
                   }
                });
    }

    // 주문 생성
    public Mono<Order> createOrder(Order order) {
        // zip 오퍼레이터 : 여러 개의 Mono나 Flux를 병렬로 실행하고 모든 결과가 완료될때까지 대기 후 실행
        return Mono.zip(
                usersRepository.existsById(order.getUserId()),
                productsRepository.existsById(order.getProductId())
        ).flatMap(tuple -> {
            if (!tuple.getT1()) {
                return Mono.error(new Exception("user not found"));
            }
            if (!tuple.getT2()) {
                return Mono.error(new Exception("product not found"));
            }
            return ordersRepository.save(order);
        });
    }

    // 주문 상세조회(유저 정보, 상품정보 포함)
    public Mono<OrderDetail> getOrderDetails(int orderId) {
        return ordersRepository.getOrderDetails(orderId);
    }

    // 주문 상태 변경
    public Mono<Order> updateOrder(int orderId, Order order) {
        return ordersRepository.findById(orderId)
                .map(existingOrder -> {
                            existingOrder.setOrderStatus(order.getOrderStatus());
                            return existingOrder;
                        })
                .switchIfEmpty(Mono.error(new Exception("order not found")))
                .flatMap(ordersRepository::save)
                .flatMap(existingOrder -> {
                    if(existingOrder.getOrderStatus() == Order.OrderStatus.SHIPPED) {
                        return ordersRepository.getOrderDetails(orderId)
                                .doOnNext(orderDetail -> {
                                    // 알림 발송
                                    Notification notification = Notification.builder()
                                            .userId(existingOrder.getUserId())
                                            .title("새 메시지")
                                            .message(orderDetail.getProductName() + " 상품이 출발하였습니다.")
                                            .createDtime(LocalDateTime.now())
                                            .build();

                                    Sinks.Many<Notification> sink = notificationManager.getSinks(existingOrder.getUserId());
                                    if(sink !=null) sink.tryEmitNext(notification);
                                })
                                .thenReturn(existingOrder);
                    }
                    return Mono.just(existingOrder);
                });
    }


}
