package com.morissoft.printing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.morissoft.printing.db.Employee;
import com.morissoft.printing.repository.EmployeeRepository;
import com.morissoft.printing.services.EmployeeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class DataTableController {
	@Autowired
	private EmployeeRepository repository;
	
	@Autowired
	private EmployeeService service;

	@RequestMapping(value = "/datatable", method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("list", service.getAllEmployees());
		return "datatable/list";
	}
	
	@RequestMapping("/datatable/add")
	public String add(Model model) {

		model.addAttribute("employee",new Employee());
		return "datatable/form";

	}

	@RequestMapping("/datatable/edit/{id}")
	public String edit(@PathVariable Long id, Model model) {

		model.addAttribute("employee", repository.findById(id).get());
		return "datatable/form";

	}

	@RequestMapping(value = "/datatable/save", method = RequestMethod.POST)
	public String save(Employee employee, final RedirectAttributes ra) {
		log.info("Employee Data {}",employee);
		Employee save = repository.saveAndFlush(employee);
		ra.addFlashAttribute("successFlash", "Employee saved successfully.");
//		applicationEventPublisher.publishEvent(new CustomerSaveCompleteEvent().setCustomerName(save.getFirstname()+" "+save.getLastname()));
		return "redirect:/datatable";

	}

	@RequestMapping("/datatable/delete/{id}")
	public String delete(@PathVariable Long id) {

		repository.deleteById(id);;
		return "redirect:/datatable";

	}
	
}
