package webflux.example.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webflux.example.model.Order;
import webflux.example.model.OrderDetail;
import webflux.example.model.Product;

public interface OrdersRepository extends ReactiveCrudRepository<Order, Integer> {

    @Query("SELECT o.id           as order_id, " +
           "       o.total_price, " +
           "       o.order_status, " +
           "       o.created_at   as order_created_at, " +
           "       o.updated_at   as order_updated_at, " +
           "       u.id           as user_id, " +
           "       u.username, " +
           "       u.email, " +
           "       u.created_at   as user_created_at, " +
           "       p.id           as product_id, " +
           "       p.name         as product_name, " +
           "       p.price        as product_price, " +
           "       p.created_at   as product_created_at " +
           "FROM orders o " +
           "JOIN users u ON o.user_id = u.id " +
           "JOIN products p ON o.product_id = p.id " +
           "WHERE o.id = :orderId")
    public Mono<OrderDetail> getOrderDetails(int orderId);

}
