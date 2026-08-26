package com.halukkilincer.userapi.service;

import com.halukkilincer.userapi.dto.CreateUserRequest;
import com.halukkilincer.userapi.dto.UpdateUserRequest;
import com.halukkilincer.userapi.dto.UserResponse;
import com.halukkilincer.userapi.entity.User;
import com.halukkilincer.userapi.exception.ConflictException;
import com.halukkilincer.userapi.exception.NotFoundException;
import com.halukkilincer.userapi.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(UserResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        return UserResponse.from(requireUser(id));
    }

    @Transactional
    public UserResponse create(CreateUserRequest request) {
        String email = request.email().trim().toLowerCase();
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new ConflictException("Email already in use: " + email);
        }
        User user = new User();
        user.setName(request.name().trim());
        user.setEmail(email);
        return UserResponse.from(userRepository.save(user));
    }

    @Transactional
    public UserResponse update(Long id, UpdateUserRequest request) {
        User user = requireUser(id);
        String email = request.email().trim().toLowerCase();
        if (userRepository.existsByEmailIgnoreCaseAndIdNot(email, id)) {
            throw new ConflictException("Email already in use: " + email);
        }
        user.setName(request.name().trim());
        user.setEmail(email);
        return UserResponse.from(userRepository.save(user));
    }

    @Transactional
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found: " + id);
        }
        userRepository.deleteById(id);
    }

    private User requireUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
    }
}
