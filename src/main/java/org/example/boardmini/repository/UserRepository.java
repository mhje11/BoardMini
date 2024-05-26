package org.example.boardmini.repository;

import org.example.boardmini.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUserIdAndPassword(String userId, String password);
    User findUserByUserId(String userId);
}
