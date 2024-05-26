package org.example.boardmini.repository;

import org.example.boardmini.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsernameAndPassword(String userId, String password);
}
