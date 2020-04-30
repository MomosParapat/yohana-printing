package com.morissoft.printing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.morissoft.printing.db.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
