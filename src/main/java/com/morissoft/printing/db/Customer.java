package com.morissoft.printing.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.morissoft.printing.base.BaseEntity;
import com.morissoft.printing.payload.CustomerPayload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@ToString
@Table(name = "customers", schema = "yohana_printing")
public class Customer extends BaseEntity {

	@Column(nullable = false)
	private String firstName;

	private String lastName;

	private String email;

	private String phoneNumber;

	private String address;

	@Column(nullable = false)
	private String status;

	@Column(nullable = false)
	private String type;

	public CustomerPayload toValueObject() {
		return new CustomerPayload().setId(this.getId()).setFirstName(this.firstName).setLastName(this.lastName)
				.setEmail(this.email).setPhoneNumber(this.phoneNumber).setAddress(this.address).setStatus(this.status)
				.setType(type);
	}
}
