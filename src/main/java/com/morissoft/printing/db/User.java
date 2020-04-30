package com.morissoft.printing.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.morissoft.printing.base.BaseEntity;
import com.morissoft.printing.payload.UserPayload;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "users", schema = "yohana_printing")
public class User extends BaseEntity {

	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	private String password;
	
	private boolean enabled;

	public UserPayload toValueObject() {
		return new UserPayload().setId(this.getId()).setUsername(username).setPassword(password).setEnabled(enabled);
	}
}
