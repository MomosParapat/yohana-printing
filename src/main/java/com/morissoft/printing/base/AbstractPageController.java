package com.morissoft.printing.base;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.morissoft.printing.db.Customer;
import com.morissoft.printing.db.Items;
import com.morissoft.printing.db.User;
import com.morissoft.printing.payload.UserPayload;
import com.morissoft.printing.services.AuthorityService;
import com.morissoft.printing.services.CustomerService;
import com.morissoft.printing.services.EmployeeService;
import com.morissoft.printing.services.InvoiceService;
import com.morissoft.printing.services.ItemService;
import com.morissoft.printing.services.PaymentService;
import com.morissoft.printing.services.ReportService;
import com.morissoft.printing.services.SalesOrderService;
import com.morissoft.printing.services.UserService;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@Slf4j
public class AbstractPageController {

	@Autowired
	protected SalesOrderService salesOrderService;

	@Autowired
	protected CustomerService customerService;

	@Autowired
	protected ItemService itemService;

	@Autowired
	protected InvoiceService invoiceService;

	@Autowired
	protected PaymentService paymentService;
	
	@Autowired
	protected EmployeeService employeeService;

	@Autowired
	protected ReportService reportService;
	
	@Autowired
	protected UserService userService;
	
	@Autowired
	protected AuthorityService authorityService;
	
	

	protected void addFormRejectedFlashAttributes(RedirectAttributes redirectAttributes, BindingResult result,
			String formName, Object form) {
		redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult." + formName, result);
		redirectAttributes.addFlashAttribute(formName, form);
	}

	protected void exportToPdf(HttpServletResponse response, String template, List objData)
			throws JRException, IOException {
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(objData);
		InputStream inputStream = this.getClass().getResourceAsStream(template);
		JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		ByteArrayOutputStream pdfReportStream = new ByteArrayOutputStream();
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfReportStream));
		exporter.exportReport();
		response.setContentType("application/pdf");
		response.setHeader("Content-Length", String.valueOf(pdfReportStream.size()));
		response.addHeader("Content-Disposition", "inline; filename=jasper.pdf;");

		OutputStream responseOutputStream = response.getOutputStream();
		responseOutputStream.write(pdfReportStream.toByteArray());
		responseOutputStream.close();
		pdfReportStream.close();
		log.info("Completed Successfully: ");

	}

	protected void exportToHtml(HttpServletResponse response, String template, List objData)
			throws JRException, IOException {
		response.setContentType("text/html");
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(objData);
		InputStream inputStream = this.getClass().getResourceAsStream(template);
		JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
		HtmlExporter exporter = new HtmlExporter(DefaultJasperReportsContext.getInstance());
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleHtmlExporterOutput(response.getWriter()));
		exporter.exportReport();

	}

	@ModelAttribute("datemodel")
	public CompletedDate dOR() {
		CompletedDate dor = new CompletedDate();
		List<Integer> dT = new ArrayList<Integer>();
		for (Integer i = 1; i < 32; i++) {

			dT.add(i);
		}
		dor.setDt(dT);

		List<month> monthList = new ArrayList<month>();
		String[] monthName = { "", "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus",
				"September", "Oktober", "November", "Desember" };
		for (int i = 1; i < 13; i++) {
			month month = new month();
			month.value = i;
			month.name = monthName[i];
			monthList.add(month);

		}
		dor.setMonth(monthList);

		int year = Calendar.getInstance().get(Calendar.YEAR);
		List<Integer> yR = new ArrayList<Integer>();
		for (Integer i = year; i < (year + 2); i++) {
			yR.add(i);
		}
		dor.setYear(yR);

		return dor;
	}

	@ModelAttribute("customers")
	public List<Customer> getCustomers() {
		return customerService.findActiveCustomers();
	}

	@ModelAttribute("items")
	public List<Items> getItems() {
		return itemService.findActiveItems();
	}

	@ModelAttribute("users")
	public List<UserPayload> getUsers() {
		return userService.findAll();
	}
	
	@Data
	public static class CompletedDate {
		private List<Integer> dt;
		private List<month> month;
		private List<Integer> year;
	}

	@Data
	public static class month {
		private Integer value;
		private String name;
	}

	@Data
	@Accessors(chain = true)
	public static class DateToDay {
		private Integer date = LocalDate.now().getDayOfMonth();
		private Integer month = LocalDate.now().getMonthValue();
		private Integer year = LocalDate.now().getYear();
	}
}
