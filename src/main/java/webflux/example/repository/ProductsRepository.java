package webflux.example.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import webflux.example.model.Product;

public interface ProductsRepository extends ReactiveCrudRepository<Product, Integer> {
}
