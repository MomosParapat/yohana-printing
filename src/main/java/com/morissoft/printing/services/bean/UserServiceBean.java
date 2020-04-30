package com.morissoft.printing.services.bean;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.morissoft.printing.db.User;
import com.morissoft.printing.payload.AuthorityPayload;
import com.morissoft.printing.payload.UserPayload;
import com.morissoft.printing.repository.UserRepository;
import com.morissoft.printing.services.AuthorityService;
import com.morissoft.printing.services.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Component
@Transactional
public class UserServiceBean implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthorityService authService;

	@Override
	public List<UserPayload> findAll() {
		return userRepository.findAll().stream().map(User::toValueObject).collect(Collectors.toList());
	}

	@Override
	public UserPayload findById(Long id) {
		UserPayload usr = findExisting(id).toValueObject();
		return usr;
	}

	@Override
	public UserPayload save(UserPayload user) {
		User maybeExist = findExisting(user.getId());
		if (maybeExist == null) {
			PasswordEncoder encoder = new BCryptPasswordEncoder();
			user.setPassword(encoder.encode(user.getPassword()));
			return userRepository.save(user.toValueEntity()).toValueObject();
		}
		return null;
	}

	private User findExisting(Long id) {
		Optional<User> findUser = userRepository.findById(id);
		if (!findUser.isPresent()) {
			return null;
		}
		return findUser.get();
	}

	@Override
	public UserPayload changePassword(String user, String password) {
		Optional<User> findUser = userRepository.findByUsername(user);
		if (findUser.isPresent()) {
			User newPass = findUser.get();
			PasswordEncoder encoder = new BCryptPasswordEncoder();
			newPass.setPassword(encoder.encode(password));
			userRepository.saveAndFlush(newPass);
		}
		return null;
	}

}
