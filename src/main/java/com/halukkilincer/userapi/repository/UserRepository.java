package com.halukkilincer.userapi.repository;

import com.halukkilincer.userapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    List<User> findAllByOrderByIdAsc();

    List<User> findByNameContainingIgnoreCaseOrderByIdAsc(String name);

    List<User> findByEmailContainingIgnoreCaseOrderByIdAsc(String email);

    List<User> findByNameContainingIgnoreCaseAndEmailContainingIgnoreCaseOrderByIdAsc(
            String name, String email);
}
