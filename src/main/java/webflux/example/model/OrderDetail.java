package webflux.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import webflux.example.model.Order.OrderStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderDetail {
    
    // 주문 정보
    private int orderId;
    private int totalPrice;
    private OrderStatus orderStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderCreatedAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderUpdatedAt;
    
    // 사용자 정보
    private int userId;
    private String username;
    private String email;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime userCreatedAt;
    
    // 상품 정보
    private int productId;
    private String productName;
    private int productPrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime productCreatedAt;
}