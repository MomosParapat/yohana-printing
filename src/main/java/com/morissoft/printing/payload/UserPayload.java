package com.morissoft.printing.payload;

import javax.persistence.Column;

import com.morissoft.printing.db.User;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserPayload {
	private Long id;
	@Column(nullable = false)
	private String username;

	private String password = "1234";

	private String status = "NEW";

	private boolean enabled = true;

	private boolean admin;
	private boolean operator;
	private boolean supervisor;

	public User toValueEntity() {
		return new User().setUsername(username).setPassword(password).setEnabled(enabled);
	}
}
