package webflux.example.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import webflux.example.model.Product;
import webflux.example.model.User;

public interface UsersRepository extends ReactiveCrudRepository<User, Integer> {
}
