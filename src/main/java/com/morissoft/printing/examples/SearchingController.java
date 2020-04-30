package com.morissoft.printing.examples;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/searching")
public class SearchingController {

	@GetMapping
	public String autocomplete(Model model) {
		model.addAttribute("title", "searching example");

		return "examples/searching";
	}

	@GetMapping("/search")
	public @ResponseBody SearchData searchPost(@RequestParam(name = "term", required = false, defaultValue = "uknown") String term) {
		log.info("searching {}", term);
		List<SearchData> retVal = data();
		for (SearchData customer : retVal) {
			if (customer.getName().equals(term)) {
				return customer;
			}
		}
		return null;
	}

	private List<SearchData> data() {
		List<SearchData> customers = new ArrayList<>();
		customers.add(new SearchData(1, "Jack"));
		customers.add(new SearchData(2, "James"));
		customers.add(new SearchData(3, "Kelly"));
		return customers;
	}

	private static class SearchData {
		private Integer id;
		private String name;

		public SearchData(Integer i, String string) {
			// TODO Auto-generated constructor stub
			this.id = i;
			this.name = string;
		}

		public String getName() {
			return name;
		}

		public Integer getId() {
			return id;
		}
	}
}
