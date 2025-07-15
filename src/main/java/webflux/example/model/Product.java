package webflux.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Slf4j
@Data
@Builder
@Table(name = "products")
public class Product {
    @Id // PK 셋팅, R2DBC에서는 복합 기본키를 지원하지않음
    private int id;
    private String name;
    private int price;
    @CreatedDate // 자동 시간 생성
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

}
