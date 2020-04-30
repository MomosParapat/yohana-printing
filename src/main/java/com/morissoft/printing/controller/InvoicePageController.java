package com.morissoft.printing.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.morissoft.printing.base.AbstractPageController;
import com.morissoft.printing.constant.Paths;
import com.morissoft.printing.db.SalesOrderDetails;
import com.morissoft.printing.payload.SalesInvoicePayload;

import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/invoices")
public class InvoicePageController extends AbstractPageController {

	@GetMapping
	public String list(Model model) {
		model.addAttribute("list", invoiceService.findAll());
		return "invoices/list";
	}

	@GetMapping("/api/{Id}")
	@ResponseBody
	public SalesInvoicePayload findByItemId(@PathVariable Long Id) {
		return invoiceService.findById(Id).toValueObject();
	}

	@GetMapping("/{Id}")
	public String findById(@PathVariable Long Id, Model model) {
		SalesInvoicePayload invoice = invoiceService.findById(Id).toValueObject();
		model.addAttribute("salesInvoicePayload", invoice);
		List<SalesOrderDetails> salesOrderDetails = salesOrderService
				.findOrderDetailsByOrderId(invoice.getOrder().getId());
		model.addAttribute("deliveryOrder", new DeliveryOrder(Id));
		model.addAttribute("details", salesOrderDetails);
		return "invoices/form";
	}

	@PostMapping
	public String deliverOrder(@Valid DeliveryOrder delivery) {
		log.info("Deliver Order {}", delivery);
		invoiceService.deliver(delivery.invoiceId);
		return "redirect:/invoices";
	}

	@ModelAttribute("title")
	public String title() {
		return Paths.SALES_INVOICE;
	}

	@Data
	public static class DeliveryOrder {
		@NonNull
		private Long invoiceId;
	}

}
