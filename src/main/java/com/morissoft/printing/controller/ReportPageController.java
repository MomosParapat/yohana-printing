package com.morissoft.printing.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.morissoft.printing.base.AbstractPageController;
import com.morissoft.printing.constant.Paths;
import com.morissoft.printing.payload.ISalesSummary;
import com.morissoft.printing.payload.PaymentPayload;
import com.morissoft.printing.payload.SalesOrderPayload;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;

@Slf4j
@Controller
@RequestMapping("/reports")
public class ReportPageController extends AbstractPageController {

	@RequestMapping(value = "/payments.html", method = RequestMethod.GET)
	public String list(Model model) {
		log.info("Payment Form {}", new PaymentForm().setDateFrom(new DateForm()).setDateTo(new DateForm()));
		model.addAttribute("paymentForm", new PaymentForm().setDateFrom(new DateForm()).setDateTo(new DateForm()));
		model.addAttribute("grandTotal", BigDecimal.ZERO);
		return "reports/payments";
	}

	@PostMapping("/payments")
	public String getReport(PaymentForm paymentForm, Model model) {
		log.info("Form {}", paymentForm);
		String dateFrom = String.format("%s-%s-%s", paymentForm.dateFrom.year, paymentForm.dateFrom.month,
				paymentForm.dateFrom.date);
		String dateTo = String.format("%s-%s-%s", paymentForm.dateTo.year, paymentForm.dateTo.month,
				paymentForm.dateTo.date);
		List<PaymentPayload> list = reportService.findPaymentByPeriod(dateFrom, dateTo);
		BigDecimal sum = list.stream().map(PaymentPayload::getPaymentAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
		model.addAttribute("payments", list);
		model.addAttribute("grandTotal", sum);
		return "reports/payments";
	}

	@GetMapping("/ar.html")
	public String getArReport(Model model) {
		List<SalesOrderPayload> list = reportService.findAccountReceivable();
		BigDecimal sum = list.stream().map(SalesOrderPayload::getTotalOutstanding).reduce(BigDecimal.ZERO,
				BigDecimal::add);
		model.addAttribute("grandTotal", sum);
		model.addAttribute("arlist", list);
		return "reports/ar";
	}

	@GetMapping("/api/ar.html")
	@ResponseBody
	public List<SalesOrderPayload> getArReportRest() {
		return reportService.findAccountReceivable();
	}

	@RequestMapping(value = "/category", method = RequestMethod.GET)
	public String category(Model model) {
		model.addAttribute("printerForm", new PrinterForm());
		return "reports/categories";
	}

	// print to html
	@GetMapping("/category.html")
	public void itemReport(HttpServletResponse response) throws Exception {
		exportToHtml(response, "/reports/rpt_category.jrxml", reportService.findCagegoryReport());
	}

	@GetMapping("/printtopdf")
	public void printPdf(HttpServletResponse response) throws JRException, IOException {
		exportToPdf(response, "/reports/rpt_category.jrxml", reportService.findCagegoryReport());
	}

	@GetMapping("/api/sales.rpt")
	@ResponseBody
	public List<ISalesSummary> getSalesReportRest() {
		String dateFrom = String.format("%s-%s-%s", 2020, 2, 1);
		String dateTo = String.format("%s-%s-%s", 2020, 2, 17);
		return reportService.findSalesByPeriod(dateFrom, dateTo);
	}

	
	@RequestMapping(value = "/sales.html", method = RequestMethod.GET)
	public String rptSummarySalesView(Model model) {
		log.info("Sales Form {}", new PaymentForm().setDateFrom(new DateForm()).setDateTo(new DateForm()));
		model.addAttribute("paymentForm", new PaymentForm().setDateFrom(new DateForm()).setDateTo(new DateForm()));
		model.addAttribute("grandTotal", BigDecimal.ZERO);
		return "reports/sales";
	}

	@PostMapping("/sales.rpt")
	public String rptSummarySalesData(PaymentForm paymentForm, Model model) {
		log.info("Form {}", paymentForm);
		String dateFrom = String.format("%s-%s-%s", paymentForm.dateFrom.year, paymentForm.dateFrom.month,
				paymentForm.dateFrom.date);
		String dateTo = String.format("%s-%s-%s", paymentForm.dateTo.year, paymentForm.dateTo.month,
				paymentForm.dateTo.date);
		List<ISalesSummary> list = reportService.findSalesByPeriod(dateFrom, dateTo);
		BigDecimal subTotal = list.stream().map(ISalesSummary::getSubTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal discount = list.stream().map(ISalesSummary::getDiscount).reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal charged = list.stream().map(ISalesSummary::getPaymentNet).reduce(BigDecimal.ZERO, BigDecimal::add);
		model.addAttribute("sales", list);
		model.addAttribute("grandSubTotal", subTotal);
		model.addAttribute("grandDiscount", discount);
		model.addAttribute("grandCharged", charged);
		return "reports/sales";
	}
	
	@Data
	@Accessors(chain = true)
	private static class PaymentForm {
		private DateForm dateFrom;
		private DateForm dateTo;
	}

	@Data
	@Accessors(chain = true)
	public static class DateForm {
		private Integer date = LocalDate.now().getDayOfMonth();
		private Integer month = LocalDate.now().getMonthValue();
		private Integer year = LocalDate.now().getYear();
	}

	@ModelAttribute("title")
	public String title() {
		return Paths.ACCOUNT_RECEIVABLES;
	}

	@Data
	@Accessors(chain = true)
	public static class PrinterForm {
		private String printerName;
	}
}
