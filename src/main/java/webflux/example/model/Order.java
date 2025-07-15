package webflux.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Slf4j
@Data
@Builder
@Table(name = "orders")
public class Order {
    @Id
    private int id;
    private int userId;
    private int productId;
    private int totalPrice;
    private OrderStatus orderStatus;
    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public enum OrderStatus {
        ORDERED, //주문완료
        SHIPPED, //배송중
        DELIVERED, //배송완료
        CANCELLED //취소
    }
}
