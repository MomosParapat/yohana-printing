package com.morissoft.printing.services.bean;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.morissoft.printing.db.Employee;
import com.morissoft.printing.payload.EmployeePayload;
import com.morissoft.printing.repository.EmployeeRepository;
import com.morissoft.printing.services.EmployeeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Component
@Transactional
public class EmployeeServiceBean implements EmployeeService {
	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public List<Employee> getAllEmployees() {
		// TODO Auto-generated method stub
		return employeeRepository.findAll();
	}

	@Override
	public EmployeePayload getById(long id) {
		return getEmployeeById(id).toValueObject();
	}

	private Employee getEmployeeById(long id) {
		Optional<Employee> optEmp = employeeRepository.findById(id);
		if (optEmp.isPresent()) {
			return optEmp.get();
		}
		return null;
	}

	@Override
	public Employee save(EmployeePayload command) throws Exception {
		// TODO Auto-generated method stub
		Employee emp = getEmployeeById(command.getId());
		if (emp == null) {
			return employeeRepository.save(command.toValueEntity());
		} else {
			emp.setEmail(command.getEmail());
			emp.setName(command.getName());
			emp.setPhone(command.getPhone());
			emp.setLastName(command.getLastName());
			emp.setAddress(command.getAddress());
			emp.setUserId(command.getUserId());
			return employeeRepository.saveAndFlush(emp);
		}
	}

}
