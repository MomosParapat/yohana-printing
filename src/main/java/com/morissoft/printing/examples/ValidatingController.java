package com.morissoft.printing.examples;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/validating")
public class ValidatingController {

	@GetMapping
	public String showForm(Model model) {
		model.addAttribute("personForm", new PersonForm());
		System.out.print("Show Form");
		return "examples/validating";
	}

	@PostMapping("/save")
	public String checkPersonInfo(@Valid PersonForm personForm, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return "examples/validating";
		}

		return "redirect:/";
	}
}
