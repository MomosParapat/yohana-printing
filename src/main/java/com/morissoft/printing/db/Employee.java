package com.morissoft.printing.db;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.morissoft.printing.base.BaseEntity;
import com.morissoft.printing.payload.EmployeePayload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@ToString
@Table(name = "employee", schema = "yohana_printing")
public class Employee extends BaseEntity {
	private String name;
	private String lastName;
	private String email;
	private String phone;
	private boolean active;
	private String address;
	private Long userId;
	
	public EmployeePayload toValueObject()
	{
		return new EmployeePayload()
				.setId(this.id)
				.setActive(this.active)
				.setEmail(this.email)
				.setId(this.getId())
				.setLastName(this.lastName)
				.setName(this.lastName)
				.setPhone(this.phone)
				.setUserId(userId)
				.setAddress(address);
	}
}
