package com.morissoft.printing.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.morissoft.printing.base.AbstractPageController;
import com.morissoft.printing.constant.Paths;
import com.morissoft.printing.db.SalesOrder;
import com.morissoft.printing.db.SalesOrderDetails;
import com.morissoft.printing.payload.ItemPricesPayload;
import com.morissoft.printing.payload.SalesOrderDetailsPayload;
import com.morissoft.printing.payload.SalesOrderPayload;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;

@Slf4j
@Controller
@RequestMapping("/salesorders")
public class SalesOrderPageController extends AbstractPageController {

	@GetMapping({ "", "/" })
	public String list(Model model) {
		model.addAttribute("list", salesOrderService.findAll());
		model.addAttribute("title", "Sales Order");
		return "salesorders/list";
	}

	@GetMapping("/add")
	public String add(Model model) {
		String orderNumber = salesOrderService.generateOrderNumber();
		DateToDay today = new DateToDay();
		SalesOrderPayload payload = new SalesOrderPayload().setId(0L).setOrderNumber(orderNumber)
				.setCompletedDate(today.getDate()).setCompletedMonth(today.getMonth())
				.setCompletedYear(today.getYear());
		model.addAttribute("salesOrderPayload", payload);
		model.addAttribute("customerName", "");
		return "salesorders/form";

	}

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable Long id, Model model) {

		SalesOrderPayload so;
		try {
			so = salesOrderService.findById(id).toValueObject();
			log.info("Sales Order {}", so);
			List<SalesOrderDetails> salesOrderDetails = salesOrderService.findOrderDetailsByOrderId(id);
			model.addAttribute("salesOrderPayload", so.setStatus("REVISED"));
			model.addAttribute("orderId", id);
			model.addAttribute("details", salesOrderDetails);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "salesorders/form";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@Valid SalesOrderPayload items, BindingResult bindingResult, final RedirectAttributes ra) {
		log.info("Items Data {}", items);
		Long salesOrderId = 0L;
		if (bindingResult.hasErrors()) {
			log.info("errors {}", bindingResult.getAllErrors());
			return "salesorders/form";
		}
		try {
			SalesOrder so = salesOrderService.save(items);
			salesOrderId = so.getId();
			ra.addFlashAttribute("successFlash", "Employee saved successfully.");
		} catch (Exception e) {
			log.error("error {},{}", e.getMessage(), e.getCause());
			ra.addFlashAttribute("errorFlash", e.getMessage());
			bindingResult.reject("InternalError");
		}
		log.info("Sales Order ID {}", salesOrderId);
		return "redirect:/salesorders/edit/" + salesOrderId;
	}

	@GetMapping("/delete")
	public String delete(@RequestParam(name = "id", required = false, defaultValue = "uknown") Long id) {
		log.info("Deleted SalesOrder {}", id);
		salesOrderService.delete(id);
		return "redirect:/salesorders";

	}

	@RequestMapping(value = "/details/{id}", method = RequestMethod.GET)
	public String priceList(@PathVariable Long id, Model model) {
		model.addAttribute("list", salesOrderService.findOrderDetailsByOrderId(id));
		model.addAttribute("itemId", id);
		return "salesorders/detailslist";
	}

	@RequestMapping(value = "/details/add/{orderId}", method = RequestMethod.GET)
	public String priceListAdd(@PathVariable Long orderId, Model model) {
		log.info("Items Price Data {}", orderId);
		model.addAttribute("salesOrderDetailsPayload", new SalesOrderDetailsPayload().setOrderId(orderId).setId(0L));
		return "salesorders/detailsform";
	}

	@RequestMapping(value = "/details/edit/{id}", method = RequestMethod.GET)
	public String priceListEdit(@PathVariable Long id, Model model) {
		log.info("Items Price Id {}", id);
		SalesOrderDetailsPayload details = salesOrderService.findOrderDetailsById(id).toValueObject();
		model.addAttribute("salesOrderDetailsPayload", details);
		model.addAttribute("orderId", details.getOrderId());
		model.addAttribute("itemId", details.getItems().getId());
		return "salesorders/detailsform";
	}

	@RequestMapping(value = "/details/save/{orderId}", method = RequestMethod.POST)
	public String savePrice(@PathVariable Long orderId, @Valid @ModelAttribute SalesOrderDetailsPayload details,
			BindingResult bindingResult, RedirectAttributes ra) {
		log.info("Items Price Data {}", details);
		if (bindingResult.hasErrors()) {
			return "salesorders/detailsform";
		}
		try {
			SalesOrderPayload so = salesOrderService.findById(orderId).toValueObject();
			details.setType(so.getCustomer().getType());
			Double qty = details.getWidth() * details.getLength();
			ItemPricesPayload itemPrices = itemService
					.findItemPriceByQtyInBetween(details.getItems().getId(), details.getQty(), details.getType())
					.toValueObject();
			details.setPrice(itemPrices.getPrice());
			details.setSubTotal(BigDecimal.valueOf(details.getQty()).multiply(details.getPrice())
					.multiply(BigDecimal.valueOf(qty > 1 ? qty : 1)));
			details.setLineTotal(BigDecimal.valueOf(details.getQty()).multiply(details.getPrice())
					.multiply(BigDecimal.valueOf(qty > 1 ? qty : 1)).subtract(details.getLineDisc()));
			salesOrderService.saveOrderDetails(details);
			ra.addFlashAttribute("successFlash", "Sales saved successfully.");
		} catch (Exception e) {
			log.error("error {}", e.getMessage());
			ra.addFlashAttribute("errorFlash", e.getMessage());
			bindingResult.reject("InternalError");
		}
		return "redirect:/salesorders/edit/" + details.getOrderId();

	}

	@GetMapping("/invoice.html/{id}")
	public String printInvoice(@PathVariable Long id, Model model) {
		model.addAttribute("invoiceId", id);
		return "salesorders/printinvoice";
	}

	@GetMapping("/invoice/{id}")
	public void printInvoice(HttpServletResponse response, @PathVariable Long id) throws JRException, IOException {
		exportToPdf(response, "/reports/invoice.jrxml", reportService.invoiceReport(id));
	}

	@GetMapping("/findbynumber")
	public @ResponseBody SalesOrder searchPost(
			@RequestParam(name = "number", required = false, defaultValue = "uknown") String number) {
		log.info("searching {}", number);
		try {
			return salesOrderService.findByOrderNumber(number);
		} catch (Exception e) {
			log.error("error {}", e.getMessage());
		}
		return null;
	}

	@ModelAttribute("title")
	public String title() {
		return Paths.SALES_ORDERS;
	}

}
