package com.morissoft.printing.services.bean;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.morissoft.printing.db.Authority;
import com.morissoft.printing.payload.AuthorityPayload;
import com.morissoft.printing.repository.AuthorityRepository;
import com.morissoft.printing.services.AuthorityService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Component
@Transactional
public class AuthorityServiceBean implements AuthorityService {

	@Autowired
	private AuthorityRepository authorityRepository;

	@Override
	public List<AuthorityPayload> findAll() {
		return authorityRepository.findAll().stream().map(Authority::toValueObject).collect(Collectors.toList());
	}

	@Override
	public List<AuthorityPayload> findByUsername(String username) {
		return authorityRepository.findByUsername(username).stream().map(Authority::toValueObject)
				.collect(Collectors.toList());
	}

	@Override
	public AuthorityPayload save(AuthorityPayload authority) {
		return authorityRepository.save(authority.toValueEntity()).toValueObject();
	}

	@Override
	public void removeOld(String userName) {
		List<AuthorityPayload> list = findByUsername(userName);
		list.forEach(aut -> {
			authorityRepository.deleteById(aut.getId());
		});
	}
}
