package com.morissoft.printing.services;

import java.util.List;

import com.morissoft.printing.payload.AuthorityPayload;

public interface AuthorityService {

	List<AuthorityPayload> findAll();

	List<AuthorityPayload> findByUsername(String username);

	AuthorityPayload save(AuthorityPayload authority);
	
	void removeOld(String userName);

}
