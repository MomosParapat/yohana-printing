package com.morissoft.printing.db;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.morissoft.printing.base.BaseEntity;
import com.morissoft.printing.payload.AuthorityPayload;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "authorities", schema = "yohana_printing")
public class Authority extends BaseEntity {
	private String username;
	private String authority;

	public AuthorityPayload toValueObject() {
		return new AuthorityPayload().setId(id).setAuthority(authority).setUsername(username);
	}
}
