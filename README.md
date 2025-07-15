# WebFlux Example Project

## 프로젝트 개요

이 프로젝트는 **사내 교육용**으로 제작된 Spring WebFlux 기반의 Reactive API 서버입니다.  
WebFlux의 핵심 개념과 SSE(Server-Sent Events)를 활용한 실시간 알림 시스템을 간단한 예제를 통해 학습할 수 있습니다.

## 기술 스택

- **Framework**: Spring Boot 2.7.8
- **Reactive**: Spring WebFlux
- **Database**: PostgreSQL + R2DBC (Reactive Database Connectivity)
- **Build Tool**: Gradle
- **Java Version**: 8
- **Documentation**: SpringDoc OpenAPI (Swagger UI)

## 프로젝트 구조

```
src/main/java/webflux/example/
├── config/          # 설정 클래스 (라우터, 에러 핸들러)
├── handler/         # WebFlux 핸들러 (API 로직)
├── model/           # 엔티티 클래스 (User, Product, Order, OrderDetail)
├── repository/      # R2DBC Repository 인터페이스
├── service/         # 비즈니스 로직
└── mange/           # SSE 알림 관리
```

## 데이터베이스 설계

### 주요 테이블
- **users**: 사용자 정보
- **products**: 상품 정보  
- **orders**: 주문 정보 (users와 products를 참조)

### 관계
- User (1) : (N) Order
- Product (1) : (N) Order

## 주요 기능

### 1. Reactive CRUD API
- **사용자 관리**: 생성, 조회, 수정, 삭제
- **주문 관리**: 생성, 상태 변경
- **상품 관리**: 조회

### 2. Join Query 예제
- 주문 상세 조회 (사용자 + 상품 정보 포함)
- R2DBC `@Query` 어노테이션 활용

### 3. SSE (Server-Sent Events)
- 주문 상태가 "SHIPPED"로 변경 시 실시간 알림 발송
- 사용자별 개별 SSE 스트림 관리

## API 엔드포인트

### 사용자 API
```
POST   /api/user           # 사용자 생성
GET    /api/users          # 전체 사용자 조회
GET    /api/user/{userId}  # 특정 사용자 조회
PUT    /api/user/{userId}  # 사용자 정보 수정
DELETE /api/user/{userId}  # 사용자 삭제
```

### 주문 API
```
POST  /api/order                        # 주문 생성
GET   /api/order/{orderId}/orderDetails  # 주문 상세 조회 (JOIN)
PATCH /api/order/{orderId}               # 주문 상태 변경
```

### SSE API
```
GET /sse/{userId}  # 실시간 알림 스트림 구독
```

## 설정 및 실행

### 1. 데이터베이스 설정
```yaml
# application.yml
spring:
  r2dbc:
    url: r2dbc:postgresql://localhost:5432/webflux_example
    username: root
    password: dlab9185
```

### 2. 샘플 데이터
```sql
-- 사용자 데이터
INSERT INTO users (username, email) VALUES ('user1', 'user1@example.com');

-- 상품 데이터
INSERT INTO products (name, price) VALUES 
('MacBook Pro 16', 2500000),
('iPhone 15 Pro', 1200000),
('AirPods Pro', 350000);
```

### 3. 애플리케이션 실행
```bash
./gradlew bootRun
```

## API 사용 예제

### 주문 생성
```bash
curl -X POST http://localhost:8080/api/order \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "productId": 2,
    "totalPrice": 1200000,
    "orderStatus": "ORDERED"
  }'
```

### 주문 상태 변경 (알림 발송)
```bash
curl -X PATCH http://localhost:8080/api/order/1 \
  -H "Content-Type: application/json" \
  -d '{
    "orderStatus": "SHIPPED"
  }'
```

### SSE 스트림 구독
```bash
curl -N http://localhost:8080/sse/1
```

## 학습 포인트

### 1. Reactive Programming
- **Mono/Flux**: 비동기 데이터 스트림 처리
- **Operators**: `map`, `flatMap`, `doOnNext`, `switchIfEmpty` 등
- **Error Handling**: `onErrorResume`, `switchIfEmpty`

### 2. WebFlux Functional Routing
- **RouterFunction**: 함수형 라우팅 설정
- **Handler**: 비즈니스 로직 분리
- **Catch-All Route**: 404 처리

### 3. R2DBC (Reactive Database)
- **Non-blocking Database Access**: 반응형 DB 연결
- **Custom Query**: `@Query` 어노테이션 활용
- **Join Query**: 복잡한 조인 쿼리 작성

### 4. SSE (Server-Sent Events)
- **Real-time Communication**: 실시간 데이터 푸시
- **Sinks**: 다중 구독자 관리
- **Event Stream**: 이벤트 기반 알림 시스템

## Swagger UI

프로젝트 실행 후 다음 URL에서 API 문서를 확인할 수 있습니다:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI Docs**: http://localhost:8080/api-docs

## 추가 학습 자료

- [Spring WebFlux 공식 문서](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html)
- [Project Reactor 문서](https://projectreactor.io/docs)
- [R2DBC 문서](https://r2dbc.io/)

---

**교육 목적**: 이 프로젝트는 Spring WebFlux의 핵심 개념을 실습을 통해 학습하기 위해 제작되었습니다.