package com.morissoft.printing.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.morissoft.printing.base.AbstractPageController;
import com.morissoft.printing.payload.AuthorityPayload;
import com.morissoft.printing.payload.UserPayload;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserPageController extends AbstractPageController {

	@RequestMapping(value = { "/", "" }, method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("list", userService.findAll());
		model.addAttribute("title", "Users");
		return "users/list";
	}

	@GetMapping("/add")
	public String add(Model model) {
		UserPayload usr = new UserPayload().setId(0L);
		model.addAttribute("user", usr);
		model.addAttribute("title", "Users");
		return "users/form";
	}

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable Long id, Model model) {
		log.info("User edit {}", id);
		try {
			UserPayload usr = userService.findById(id);
			List<AuthorityPayload> authList = authorityService.findByUsername(usr.getUsername());
			log.info("Auth List {}", authList);
			usr.setAdmin(authList.stream().filter(p -> p.getAuthority().equalsIgnoreCase("ROLE_ADMIN")).findAny()
					.isPresent());
			usr.setOperator(authList.stream().filter(p -> p.getAuthority().equalsIgnoreCase("ROLE_OPERATOR")).findAny()
					.isPresent());
			usr.setSupervisor(authList.stream().filter(p -> p.getAuthority().equalsIgnoreCase("ROLE_SUPERVISOR"))
					.findAny().isPresent());
			log.info("User {}", usr);
			model.addAttribute("user", usr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "users/form";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@ModelAttribute("user") UserPayload user, final RedirectAttributes ra) {
		log.info("User Data {}", user);
		try {
			userService.save(user);
			authorityService.removeOld(user.getUsername());
			if (user.isAdmin()) {
				AuthorityPayload auth = new AuthorityPayload().setUsername(user.getUsername())
						.setAuthority("ROLE_ADMIN");
				authorityService.save(auth);
			}
			if (user.isOperator()) {
				AuthorityPayload auth = new AuthorityPayload().setUsername(user.getUsername())
						.setAuthority("ROLE_OPERATOR");
				authorityService.save(auth);
			}
			if (user.isSupervisor()) {
				AuthorityPayload auth = new AuthorityPayload().setUsername(user.getUsername())
						.setAuthority("ROLE_SUPERVISOR");
				authorityService.save(auth);
			}
			ra.addFlashAttribute("successFlash", "User saved successfully.");
		} catch (Exception e) {
			ra.addFlashAttribute("errorFlash", e.getMessage());
		}
		return "redirect:/users";
	}

	@ModelAttribute("authorities")
	private Authority[] authList() {
		List<Authority> auths = new ArrayList<UserPageController.Authority>();
		Authority auth1 = new Authority();
		auth1.setName("OPERATOR");
		auth1.setValue("ROLE_OPERATOR");
		auths.add(auth1);

		auth1 = new Authority();
		auth1.setName("SUPERVISOR");
		auth1.setValue("ROLE_SUPERVISOR");
		auths.add(auth1);

		auth1 = new Authority();
		auth1.setName("ADMIN");
		auth1.setValue("ROLE_ADMIN");
		auths.add(auth1);
		Authority[] authorities = new Authority[auths.size()];
		return auths.toArray(authorities);
	}

	@Data
	private static class Authority {
		private String value;
		private String name;
	}
}
