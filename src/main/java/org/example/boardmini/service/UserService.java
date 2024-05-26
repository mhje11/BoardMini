package org.example.boardmini.service;

import lombok.RequiredArgsConstructor;
import org.example.boardmini.domain.User;
import org.example.boardmini.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
    }
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    public User findByUsernameAndPassword(String userId, String password) {
        return userRepository.findByUsernameAndPassword(userId, password);
    }
}
