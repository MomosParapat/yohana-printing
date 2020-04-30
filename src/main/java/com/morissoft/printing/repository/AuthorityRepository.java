package com.morissoft.printing.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.morissoft.printing.db.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
	List<Authority> findByUsername(String username);

	Optional<Authority> findByUsernameAndAuthority(String userName, String authority);
}
