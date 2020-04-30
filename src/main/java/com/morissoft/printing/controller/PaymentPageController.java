package com.morissoft.printing.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.morissoft.printing.base.AbstractPageController;
import com.morissoft.printing.db.Payments;
import com.morissoft.printing.db.SalesInvoice;
import com.morissoft.printing.db.SalesOrder;
import com.morissoft.printing.db.SalesOrderDetails;
import com.morissoft.printing.payload.CustomerPayload;
import com.morissoft.printing.payload.PaymentPayload;
import com.morissoft.printing.payload.SalesInvoicePayload;
import com.morissoft.printing.payload.SalesOrderPayload;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;

@Slf4j
@Controller
@RequestMapping("/payments")
public class PaymentPageController extends AbstractPageController {

	@RequestMapping(value = { "/", "" }, method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("list", paymentService.findAll());
		return "payments/list";
	}

	@RequestMapping("/add")
	public String add(Model model) {
		String paymentNumber = paymentService.generatePaymentNumber();
		PaymentPayload payload = new PaymentPayload()
				.setInvoice(
						new SalesInvoicePayload().setOrder(new SalesOrderPayload().setCustomer(new CustomerPayload())))
				.setPaymentNumber(paymentNumber);
		log.info("paymentPayload : {}", payload);
		model.addAttribute("paymentPayload", payload);
		return "payments/form";

	}

	@PostMapping("/save")
	public String save(@Valid PaymentPayload form, BindingResult bindingResult, final RedirectAttributes ra) {
		log.info("Payments Data {}", form);
		if (bindingResult.hasErrors()) {
			log.info("errors {}", bindingResult.getAllErrors());
			return "payments/form-two";
		}
		try {
			paymentService.save(form);
			ra.addFlashAttribute("successFlash", "payment saved successfully.");
		} catch (Exception e) {
			ra.addFlashAttribute("errorFlash", e.getMessage());
			bindingResult.reject("InternalError");
		}
		return "redirect:/payments";
	}

	@RequestMapping("/sales/{orderId}")
	public String payment(@PathVariable Long orderId, Model model) throws Exception {
		String paymentNumber = paymentService.generatePaymentNumber();
		SalesOrder so = salesOrderService.findById(orderId);
		SalesInvoice inv = invoiceService.findByActiveStatus(so);
		PaymentPayload payload = new PaymentPayload();
		if (inv == null) {
			payload.setInvoice(
					new SalesInvoicePayload().setOrderNumber(so.getOrderNumber()).setOrder(so.toValueObject()));
		} else {
			log.info("existing salesInvoice {}", inv.toValueObject());
			payload.setInvoice(inv.toValueObject());
		}
		payload.setPaymentNumber(paymentNumber);
		payload.setOrderNumber(so.getOrderNumber());
		List<SalesOrderDetails> salesOrderDetails = salesOrderService.findOrderDetailsByOrderId(orderId);

		log.info("paymentPayload : {}", payload);
		model.addAttribute("paymentPayload", payload);
		model.addAttribute("details", salesOrderDetails);
		model.addAttribute("title", "Payment Form");
		return "payments/form-two";

	}

	@PostMapping("/save/{orderId}")
	public String saveTwo(@PathVariable Long orderId, @Valid PaymentPayload form, BindingResult bindingResult,
			final RedirectAttributes ra) {
		log.info("Payments Data {}", form);

		if (form.getInvoice().getOrder().getTotalOutstanding().doubleValue() <= 0) {
			ra.addFlashAttribute("errorFlash",
					String.format("invoice: %s is no longer active.", form.getOrderNumber()));
			return "redirect:/payments/sales/" + orderId;
		}

		if (form.getPaymentAmount().doubleValue() > form.getInvoice().getOrder().getTotalOutstanding().doubleValue()) {
			ra.addFlashAttribute("errorFlash", "payment amount must be less than or equal to total_due.");
			return "redirect:/payments/sales/" + orderId;
		}

		if (bindingResult.hasErrors()) {
			return "redirect:/payments/sales/" + orderId;
		}
		try {
			paymentService.save(form);
			ra.addFlashAttribute("successFlash", "payment saved successfully.");
		} catch (Exception e) {
			ra.addFlashAttribute("errorFlash", e.getMessage());
			bindingResult.reject("InternalError");
		}
		return "redirect:/payments";
	}

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable Long id, Model model, final RedirectAttributes ra) {
		try {
			log.info("find a payment by id {}", id);
			Payments py = paymentService.findById(id);
			model.addAttribute("paymentPayload", py.toValueObject());
			log.info("Payment {}", py.toValueObject());
		} catch (Exception e) {
			ra.addFlashAttribute("successFlash", e.getMessage());
//			e.printStackTrace();
		}
		return "payments/form";
	}

	@RequestMapping("/delete")
	public String delete(@PathVariable Long id) {
		log.info("delete payment {}", id);
		return "redirect:/payments";

	}

	@GetMapping("/findbyid")
	public @ResponseBody PaymentPayload searchPost(
			@RequestParam(name = "id", required = false, defaultValue = "uknown") Long id) {
		log.info("searching {}", id);
		try {
			return paymentService.findById(id).toValueObject();
		} catch (Exception e) {
			log.error("error {}", e.getMessage());
		}
		return null;
	}

	@RequestMapping("/process.html")
	public String process(@RequestParam(name = "id", required = false, defaultValue = "uknown") Long id) {
		log.info("process payment {}", id);
		paymentService.processByPaymentId(id);
		return "redirect:/payments";
	}

// asynchronous
	@RequestMapping("/void.html")
	public String paymentVoidAsync(@RequestParam(name = "id", required = false, defaultValue = "uknown") Long id) {
		log.info("void payment {}", id);
		paymentService.voidPayment(id);
		return "redirect:/payments";
	}

	@RequestMapping("/void.html/{id}")
	public String paymentVoid(@PathVariable Long id) {
		log.info("void payment {}", id);
		paymentService.voidPayment(id);
		return "redirect:/payments";
	}

	@GetMapping("/receipt.html/{id}")
	public String printReceipt(@PathVariable Long id, Model model) {
		model.addAttribute("paymentId", id);
		return "payments/printreceipt";
	}

	@GetMapping("/receipt/{id}")
	public void printReceipt(HttpServletResponse response, @PathVariable Long id) throws JRException, IOException {
		exportToPdf(response, "/reports/payment.jrxml", reportService.receiptReport(id));
	}

}
