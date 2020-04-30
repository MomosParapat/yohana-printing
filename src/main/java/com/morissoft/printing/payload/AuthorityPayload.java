package com.morissoft.printing.payload;


import com.morissoft.printing.db.Authority;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AuthorityPayload {
	private Long id;
	private String username;

	private String authority;
	
	private boolean checked=false;

	public Authority toValueEntity() {
		return new Authority().setUsername(username).setAuthority(authority);
	}
}
