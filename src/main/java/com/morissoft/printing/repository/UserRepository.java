package com.morissoft.printing.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.morissoft.printing.db.User;

public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findByUsername(String username);
}
