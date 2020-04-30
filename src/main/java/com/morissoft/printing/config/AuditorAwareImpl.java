package com.morissoft.printing.config;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditorAwareImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		return Optional
				.of(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
		// return Optional.of(System.getProperty("user.name"));
		// return Optional.of("naruto");
	}
}
