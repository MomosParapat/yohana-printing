package com.morissoft.printing.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.morissoft.printing.base.AbstractPageController;
import com.morissoft.printing.constant.Paths;
import com.morissoft.printing.payload.ItemPricesPayload;
import com.morissoft.printing.payload.ItemsPayload;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/items")
public class ItemPageController extends AbstractPageController {

	@RequestMapping(value = {"/",""}, method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("list", itemService.findAllItems());
		return "items/list";
	}

	@RequestMapping("/add")
	public String add(Model model) {
		model.addAttribute("item", new ItemsPayload().setId(0L));
		model.addAttribute("category", itemService.findAllCategories());
		model.addAttribute("formType", "1");
		return "items/form";

	}

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable Long id, Model model) {

		model.addAttribute("item", itemService.findById(id).toValueObject());
		model.addAttribute("category", itemService.findAllCategories());
		model.addAttribute("formType", "2");
		return "items/form";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(ItemsPayload items, final RedirectAttributes ra) {
		log.info("Items Data {}", items);
		try {
			itemService.save(items);
			ra.addFlashAttribute("successFlash", "Employee saved successfully.");
		} catch (Exception e) {
			ra.addFlashAttribute("errorFlash", e.getMessage());
		}
		return "redirect:/items";
	}

	@RequestMapping("/delete")
	public String delete(@RequestParam(name = "id", required = false, defaultValue = "uknown") Long id) {
		log.info("Deleted Item {}", id);
		itemService.delete(id);
		return "redirect:/items";

	}

	@RequestMapping(value = "/pricelist/{id}", method = RequestMethod.GET)
	public String priceList(@PathVariable Long id, Model model) {
		model.addAttribute("list", itemService.findItemPricesByItemId(id));
		model.addAttribute("item", itemService.findById(id));
		model.addAttribute("itemId", id);
		return "items/pricelist";
	}

	@RequestMapping(value = "/pricelist/add/{itemId}", method = RequestMethod.GET)
	public String priceListAdd(@PathVariable Long itemId, Model model) {
		log.info("Items Price Data {}", itemId);
		model.addAttribute("itemPricesPayload", new ItemPricesPayload().setItemId(itemId).setId(0L));
		return "items/priceform";
	}

	@RequestMapping(value = "/pricelist/edit/{id}", method = RequestMethod.GET)
	public String priceListEdit(@PathVariable Long id, Model model) {
		log.info("Items Price Id {}", id);
		ItemPricesPayload price = itemService.findItemPriceById(id).toValueObject();
		model.addAttribute("itemPricesPayload", price);
		model.addAttribute("itemId", price.getItemId());
		return "items/priceform";
	}

	@RequestMapping(value = "/pricelist/save/{itemId}", method = RequestMethod.POST)
	public String savePrice(@PathVariable Long itemId, @Valid ItemPricesPayload itemPrices, BindingResult bindingResult,
			final RedirectAttributes ra) {
		log.info("Items Price Data {}", itemPrices);
		if (bindingResult.hasErrors()) {
			log.info("errors {}", bindingResult.getAllErrors());
			return "items/priceform";
		}
		try {
			itemService.addItemPrice(itemPrices);
			ra.addFlashAttribute("successFlash", "Employee saved successfully.");
		} catch (Exception e) {
			log.error("error {}", e.getMessage());
			ra.addFlashAttribute("errorFlash", e.getMessage());
			bindingResult.reject("InternalError");
		}
		return "redirect:/items/pricelist/" + itemId;

	}

	@RequestMapping(value = "/pricelist/price", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
//	public ItemPricesPayload getPrice(PriceRequest request) throws Exception {
//		return itemService.findItemPriceByQtyInBetween(request.getItemId(), request.getQty()).toValueObject();
//	}
	public List<ItemsPayload> getPrice(@RequestParam("searchstr") String searchstr) throws Exception {
		log.info("Search {}", searchstr);
		List<ItemsPayload> items = itemService.findAllItems().stream().map(x -> x.toValueObject())
				.collect(Collectors.toList());
		return items.stream().filter(x -> x.getName().contains(searchstr)).collect(Collectors.toList());
	}

	@Data
	private static class PriceRequest {
		private Long itemId;
		private Integer qty;
	}
	
	@ModelAttribute("title")
	public String title() {
		return Paths.ITEMS;
	}

}
