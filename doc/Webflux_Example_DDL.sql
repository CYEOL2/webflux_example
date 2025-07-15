-- 사용자 테이블
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 상품 테이블  
CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 주문 테이블
CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    total_price INTEGER NOT NULL,
    order_status VARCHAR(20) NOT NULL DEFAULT 'ORDERED', -- ORDERED(주문완료), SHIPPED(배송중), DELIVERED(배송완료) 
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


INSERT INTO products (id, name, price, created_at) VALUES (1, 'MacBook Pro 16', 2500000, NOW());
INSERT INTO products (id, name, price, created_at) VALUES (2, 'iPhone 15 Pro', 1200000, NOW());
INSERT INTO products (id, name, price, created_at) VALUES (3, 'AirPods Pro', 350000, NOW());
