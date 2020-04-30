package com.morissoft.printing.payload;

import com.morissoft.printing.db.Employee;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EmployeePayload {
	private Long Id;
	private String name;
	private String lastName;
	private String email;
	private String phone;
	private boolean active;
	private String address;
	
	private Long userId;

	public Employee toValueEntity() {
		return new Employee().setName(this.name).setEmail(this.email).setLastName(this.lastName).setActive(this.active)
				.setPhone(this.phone).setUserId(userId).setAddress(address);
	}
}
