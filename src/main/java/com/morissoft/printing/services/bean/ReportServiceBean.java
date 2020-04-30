package com.morissoft.printing.services.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.ibm.icu.text.DecimalFormat;
import com.morissoft.printing.db.Categories;
import com.morissoft.printing.db.Payments;
import com.morissoft.printing.db.SalesOrder;
import com.morissoft.printing.db.SalesOrderDetails;
import com.morissoft.printing.payload.ISalesSummary;
import com.morissoft.printing.payload.PaymentPayload;
import com.morissoft.printing.payload.SalesOrderDetailsPayload;
import com.morissoft.printing.payload.SalesOrderPayload;
import com.morissoft.printing.repository.CategoriesRepository;
import com.morissoft.printing.repository.PaymentsRepository;
import com.morissoft.printing.repository.SalesInvoiceRepository;
import com.morissoft.printing.repository.SalesOrderDetailsRepository;
import com.morissoft.printing.repository.SalesOrderRepository;
import com.morissoft.printing.services.ReportService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Component
public class ReportServiceBean implements ReportService {

	@Autowired
	private PaymentsRepository paymentsRepository;

	@Autowired
	private SalesInvoiceRepository salesInvoiceRepository;

	@Autowired
	private SalesOrderRepository salesOrderRepository;

	@Autowired
	private SalesOrderDetailsRepository detailRepository;

	@Autowired
	private CategoriesRepository categoriesRepository;

	@Override
	public List<PaymentPayload> findPaymentByPeriod(String dateFrom, String dateTo) {
		return paymentsRepository.findByPeriod(dateFrom, dateTo).stream().map(Payments::toValueObject)
				.collect(Collectors.toList());
	}

	@Override
	public List<SalesOrderPayload> findAccountReceivable() {
		return salesOrderRepository.findAccountReceivableReport().stream().map(SalesOrder::toValueObject)
				.collect(Collectors.toList());
	}

	@Override
	public List<Map<String, Object>> findCagegoryReport() {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for (Categories cat : categoriesRepository.findAll()) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("id", cat.getId());
			item.put("name", cat.getName());
			item.put("createdBy", cat.getCreatedBy());
			result.add(item);
		}
		log.info("Categories Report {}", result);
		return result;
	}

	@Override
	public List<Map<String, Object>> invoiceReport(Long id) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		SalesOrderPayload so = salesOrderRepository.findById(id).map(SalesOrder::toValueObject).get();
		List<SalesOrderDetailsPayload> details = detailRepository.findByOrderId(id).stream()
				.map(SalesOrderDetails::toValueObject).collect(Collectors.toList());
		for (SalesOrderDetailsPayload det : details) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("customerName", so.getCustomer().getFirstName() + " " + so.getCustomer().getLastName());
			item.put("customerEmail", so.getCustomer().getEmail());
			item.put("invoiceNumber", so.getOrderNumber());
			item.put("invoiceAmount", so.getTotalOutstanding());
			item.put("totalPaid", so.getTotalPaid());
			item.put("invoiceDate",
					String.format("%s-%s-%s", so.getCompletedDate(), so.getCompletedMonth(), so.getCompletedYear()));
			item.put("poNumber", so.getOrderNumber());
			item.put("paymentType", "CASH");
			item.put("itemNo", det.getItems().getCode());
			item.put("productName", det.getItems().getName());
			item.put("price", det.getPrice());
			item.put("qty", det.getQty());
			item.put("lineTotal", det.getLineTotal());
			result.add(item);
		}
		log.info("Sales Invoice Report {}", result);
		return result;
	}

	@Override
	public List<ISalesSummary> findSalesByPeriod(String dateFrom, String dateTo) {
		return salesOrderRepository.findSalesByPeriod(dateFrom, dateTo);
	}

	@Override
	public List<Map<String, Object>> receiptReport(Long id) {
		Payments payment = paymentsRepository.findById(id).get();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		SalesOrderPayload so = payment.getInvoice().getOrder().toValueObject();
		List<SalesOrderDetailsPayload> details = detailRepository.findByOrderId(payment.getInvoice().getOrder().getId())
				.stream().map(SalesOrderDetails::toValueObject).collect(Collectors.toList());

		for (SalesOrderDetailsPayload det : details) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("customerName", so.getCustomer().getFirstName() + " " + so.getCustomer().getLastName());
			item.put("paymentNumber", payment.getPaymentNumber());
			item.put("invoiceNumber", so.getOrderNumber());
			item.put("invoiceAmount", so.getTotalOutstanding());
			item.put("discount", so.getDiscount());
			item.put("totalPaid", payment.getPaymentAmount());
			item.put("invoiceDate",
					String.format("%s-%s-%s", so.getCompletedDate(), so.getCompletedMonth(), so.getCompletedYear()));
			item.put("operator", payment.getCreatedBy());
			item.put("productName", det.getItems().getName());
			item.put("quantity",
					(det.getWidth() * det.getLength()) > 0 ? String.format("(%s x %s)", det.getLength(), det.getWidth())
							: "");
			item.put("price", String.format("%s x %s", det.getQty(),new DecimalFormat("#,##0").format(det.getPrice())));
			item.put("lineTotal", det.getLineTotal());
			result.add(item);
		}
		log.info("Receipt Report {}", result);
		return result;
	}
}
