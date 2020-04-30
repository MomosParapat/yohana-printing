package com.morissoft.printing.services;

import java.util.List;

import com.morissoft.printing.payload.UserPayload;

public interface UserService {

	List<UserPayload> findAll();

	UserPayload findById(Long id);

	UserPayload save(UserPayload user);

	UserPayload changePassword(String user, String password);
}
