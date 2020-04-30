package com.morissoft.printing.payload;

import com.morissoft.printing.db.Customer;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CustomerPayload {
	private Long Id;

	private String firstName;

	private String lastName;

	private String email;

	private String phoneNumber;

	private String address;

	private String status = "ACTIVE";

	private String type;

	public Customer toValueEntity() {
		return new Customer().setAddress(this.address).setEmail(this.email).setFirstName(this.firstName)
				.setLastName(this.lastName).setPhoneNumber(this.phoneNumber).setStatus(status).setType(type);
	}
}
