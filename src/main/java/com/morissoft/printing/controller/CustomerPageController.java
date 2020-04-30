package com.morissoft.printing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.morissoft.printing.base.AbstractPageController;
import com.morissoft.printing.constant.Paths;
import com.morissoft.printing.payload.CustomerPayload;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/customers")
public class CustomerPageController extends AbstractPageController {

	@RequestMapping(value = { "/", "" }, method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("list", customerService.findAll());
		return "customers/list";
	}

	@RequestMapping("/add")
	public String add(Model model) {
		model.addAttribute("customer", new CustomerPayload().setId(0L));
		model.addAttribute("formType", "1");
		return "customers/form";

	}

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable Long id, Model model) {
		try {
			model.addAttribute("customer", customerService.findById(id).toValueObject());
			model.addAttribute("formType", "2");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "customers/form";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(CustomerPayload items, final RedirectAttributes ra) {
		log.info("Customer Data {}", items);
		try {
			customerService.save(items);
			ra.addFlashAttribute("successFlash", "Employee saved successfully.");
		} catch (Exception e) {
			ra.addFlashAttribute("errorFlash", e.getMessage());
		}
		return "redirect:/customers";
	}

	@RequestMapping("/delete")
	public String delete(@RequestParam(name = "id", required = false, defaultValue = "uknown") Long id) {
		log.info("Deleted Customer {}", id);
		customerService.delete(id);
		return "redirect:/customers";

	}

	@ModelAttribute("title")
	public String title() {
		return Paths.CUSTOMERS;
	}

}
