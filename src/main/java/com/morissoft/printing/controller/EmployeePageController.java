package com.morissoft.printing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.morissoft.printing.base.AbstractPageController;
import com.morissoft.printing.payload.EmployeePayload;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/employees")
public class EmployeePageController extends AbstractPageController {

	@RequestMapping({ "/", "" })
	public String index() {
		return "employees/list";
	}

	@RequestMapping("/add")
	public String add(Model model) {
		model.addAttribute("employeePayload", new EmployeePayload().setId(0L));
		return "employees/form";
	}

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable Long id, Model model) {
		EmployeePayload pl = employeeService.getById(id);
		log.info("Edit Employee {}", pl);
		model.addAttribute("employeePayload", pl);
		return "employees/form";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(EmployeePayload items, final RedirectAttributes ra) {
		log.info("Employee Data {}", items);
		try {
			employeeService.save(items);
			ra.addFlashAttribute("successFlash", "Employee saved successfully.");
		} catch (Exception e) {
			ra.addFlashAttribute("errorFlash", e.getMessage());
		}
		return "redirect:/employees";
	}

	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable Long id) {
		return "redirect:/employees";

	}

}
