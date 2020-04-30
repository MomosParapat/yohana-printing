package com.morissoft.printing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.morissoft.printing.db.Employee;
import com.morissoft.printing.repository.EmployeeRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ExamplePageController {
	@Autowired
	private EmployeeRepository repository;
	
    @RequestMapping("/example")
    public String employee() {
		return "redirect:/example/1";
    }

	@RequestMapping(value = "/example/{pageNumber}", method = RequestMethod.GET)
	public String list(@PathVariable Integer pageNumber, Model model) {
		Pageable paging = PageRequest.of(pageNumber-1, 10, Sort.by("id"));

		Page<Employee> page = repository.findAll(paging);

		int current = page.getNumber()+1;
		int begin = Math.max(1, current - 10);
		int end = Math.min(begin + 20, page.getTotalPages());
		model.addAttribute("list", page);
		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);

		return "examples/list";
	}
	
	@RequestMapping("/example/add")
	public String add(Model model) {

		model.addAttribute("employee", new Employee());
		return "examples/form";

	}

	@RequestMapping("/example/edit/{id}")
	public String edit(@PathVariable Long id, Model model) {

		model.addAttribute("employee", repository.findById(id).get());
		return "examples/form";

	}

	@RequestMapping(value = "/example/save", method = RequestMethod.POST)
	public String save(Employee employee, final RedirectAttributes ra) {
		log.info("Employee Data {}",employee);
		Employee save = repository.saveAndFlush(employee);
		ra.addFlashAttribute("successFlash", "Employee saved successfully.");
//		applicationEventPublisher.publishEvent(new CustomerSaveCompleteEvent().setCustomerName(save.getFirstname()+" "+save.getLastname()));
		return "redirect:/example";

	}

	@RequestMapping("/example/delete/{id}")
	public String delete(@PathVariable Long id) {

		repository.deleteById(id);;
		return "redirect:/example";

	}
	
}
