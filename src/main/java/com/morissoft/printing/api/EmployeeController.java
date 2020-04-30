package com.morissoft.printing.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.morissoft.printing.db.Employee;
import com.morissoft.printing.db.ItemPrices;
import com.morissoft.printing.payload.EmployeePayload;
import com.morissoft.printing.payload.ItemPricesPayload;
import com.morissoft.printing.payload.SalesOrderPayload;
import com.morissoft.printing.repository.EmployeeRepository;
import com.morissoft.printing.services.EmployeeService;
import com.morissoft.printing.services.ItemService;
import com.morissoft.printing.services.SalesOrderService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class EmployeeController {
	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private ItemService itemService;
	
	@Autowired
	private SalesOrderService salesOrderService;

	@RequestMapping(path = "/employeelist", method = RequestMethod.GET)
	public List<Employee> getAllEmployees() {
		return employeeService.getAllEmployees();
	}

	@RequestMapping(value = "/employee/{id}", method = RequestMethod.GET)
	public EmployeePayload getEmployeeById(@PathVariable("id") long id) {
		return employeeService.getById(id);
	}
	
	@GetMapping("/salesorders/all")
	public List<SalesOrderPayload>getAll()
	{
		return salesOrderService.findAllSalesOrder();
	}
}
