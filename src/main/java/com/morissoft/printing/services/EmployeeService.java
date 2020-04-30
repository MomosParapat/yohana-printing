package com.morissoft.printing.services;

import java.util.List;

import com.morissoft.printing.db.Employee;
import com.morissoft.printing.payload.EmployeePayload;

public interface EmployeeService {
	List<Employee> getAllEmployees();

	EmployeePayload getById(long id);

	Employee save(EmployeePayload command) throws Exception;

}
