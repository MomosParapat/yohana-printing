package com.morissoft.printing.services.bean;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.morissoft.printing.db.Payments;
import com.morissoft.printing.db.SalesInvoice;
import com.morissoft.printing.db.SalesOrder;
import com.morissoft.printing.payload.PaymentPayload;
import com.morissoft.printing.repository.PaymentsRepository;
import com.morissoft.printing.repository.SalesInvoiceRepository;
import com.morissoft.printing.services.InvoiceService;
import com.morissoft.printing.services.PaymentService;
import com.morissoft.printing.services.SalesOrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Component
@Transactional
public class PaymentServiceBean implements PaymentService {
	@Autowired
	private PaymentsRepository paymentsRepository;

	@Autowired
	private SalesInvoiceRepository salesInvoiceRepository;

	@Autowired
	private InvoiceService invoiceService;

	@Autowired
	private SalesOrderService salesOrderService;

	@Override
	public List<Payments> findAll() {
		return paymentsRepository.findAll();
	}

	@Override
	public Payments save(PaymentPayload command) throws Exception {
		Optional<Payments> optPayment = paymentsRepository.findById(command.getId());
		SalesOrder so = salesOrderService.findByOrderNumber(command.getOrderNumber());
		SalesInvoice inv = invoiceService.findByActiveStatus(so);
		if (inv == null) {
			log.info("Add new Sales Invoice {}", command);
			so.setStatus("ISSUED");
			so.setTotalPaid(command.getPaymentAmount());
			so.setTotalOutstanding(so.getPaymentNet().subtract(so.getTotalPaid()));
			inv = new SalesInvoice();
			inv.setOrder(so);
			inv.setCompletedDate(so.getCompletedDate());
			inv.setDiscount(so.getDiscount());
			inv.setPaymentNet(so.getPaymentNet());
			inv.setSubTotal(so.getSubTotal());
			inv.setOrderNumber(so.getOrderNumber());
			inv.setStatus(so.getTotalOutstanding().doubleValue() > 0 ? "ACTIVE" : "PAID");
			inv = salesInvoiceRepository.save(inv);
		} else {
			log.info("Updating Sales Invoice {}", command);
			so.setTotalPaid(so.getTotalPaid().add(command.getPaymentAmount()));
			so.setTotalOutstanding(so.getPaymentNet().subtract(so.getTotalPaid()));
			inv.setStatus(so.getTotalOutstanding().doubleValue() > 0 ? "ACTIVE" : "PAID");
		}
		if (optPayment.isPresent()) {
			Payments payment = optPayment.get();
			log.info("Updating Payment {}", payment);
			payment.setInvoice(inv);
			payment.setPaymentAmount(command.getPaymentAmount());
			payment.setPaymentNumber(command.getPaymentNumber());
			payment.setStatus("REVISED");
			return paymentsRepository.saveAndFlush(payment);
		} else {
//			String paymentNumber = generatePaymentNumber();
//			command.setPaymentNumber(paymentNumber);
			log.info("Add new Payment {}", command);
			return paymentsRepository.save(command.toValueEntity().setInvoice(inv));
		}
	}

	@SuppressWarnings("unused")
	private Payments saveOld(PaymentPayload command) throws Exception {
		Optional<Payments> optPayment = paymentsRepository.findById(command.getId());
		SalesOrder so = salesOrderService.findByOrderNumber(command.getOrderNumber());
		Optional<SalesInvoice> optInv = salesInvoiceRepository.findByOrder(so);
		SalesInvoice inv;
		if (!optInv.isPresent()) {
			log.info("Add new Sales Invoice {}", command);
			so.setStatus("ISSUED");
			so.setTotalPaid(command.getPaymentAmount());
			so.setTotalOutstanding(so.getPaymentNet().subtract(so.getTotalPaid()));

			inv = new SalesInvoice();
			inv.setOrder(so);
			inv.setCompletedDate(so.getCompletedDate());
			inv.setDiscount(so.getDiscount());
			inv.setPaymentNet(so.getPaymentNet());
			inv.setSubTotal(so.getSubTotal());
			inv.setOrderNumber(so.getOrderNumber());
			inv.setStatus(so.getTotalOutstanding().doubleValue() > 0 ? "ACTIVE" : "PAID");
			inv = salesInvoiceRepository.save(inv);
		} else {
			log.info("Updating Sales Invoice {}", command);
			inv = optInv.get();
			so.setTotalPaid(so.getTotalPaid().add(command.getPaymentAmount()));
			so.setTotalOutstanding(so.getPaymentNet().subtract(so.getTotalPaid()));
			inv.setStatus(so.getTotalOutstanding().doubleValue() > 0 ? "ACTIVE" : "PAID");
		}
		if (optPayment.isPresent()) {
			Payments payment = optPayment.get();
			log.info("Updating Payment {}", payment);
			payment.setInvoice(optInv.get());
			payment.setPaymentAmount(command.getPaymentAmount());
			payment.setPaymentNumber(command.getPaymentNumber());
			payment.setStatus("REVISED");
			return paymentsRepository.saveAndFlush(payment);
		} else {
			log.info("Add new Payment {}", command);
			return paymentsRepository.save(command.toValueEntity().setInvoice(inv));
		}
	}

	@Override
	public Payments findById(Long id) throws Exception {
		Payments payments = findExisting(id);
		if (payments == null) {
			throw new Exception("Payment with id " + id + "does not exist");
		}
		return payments;
	}

	private Payments findExisting(Long id) {
		Optional<Payments> payments = paymentsRepository.findById(id);
		if (payments.isPresent()) {
			return payments.get();
		}
		return null;
	}

	@Override
	public Payments voidPayment(Long id) {
		Optional<Payments> optPayment = paymentsRepository.findById(id);
		if (optPayment.isPresent()) {
			Payments payment = optPayment.get();
			payment.setStatus("VOIDED");
			SalesInvoice inv = payment.getInvoice();
			SalesOrder so = inv.getOrder();
			so.setTotalPaid(so.getTotalPaid().subtract(payment.getPaymentAmount()));
			so.setTotalOutstanding(so.getPaymentNet().subtract(so.getTotalPaid()));
			inv.setStatus(so.getTotalPaid().doubleValue() > 0 ? "ACTIVE" : "VOIDED");
			so.setStatus(inv.getStatus().equalsIgnoreCase("VOIDED") ? "REVISED" : "ISSUED");
			return paymentsRepository.saveAndFlush(payment);
		}
		return null;
	}

	@Override
	public SalesInvoice processByInvoice(Long Id) {
		SalesInvoice inv = invoiceService.findById(Id);
		inv.setStatus("DELIVERED");
		List<Payments> payments = paymentsRepository.findByInvoice(inv);
		payments.forEach(payment -> {
			if (payment.getStatus().equalsIgnoreCase("NEW")) {
				payment.setStatus("PROCESSED");
			}
			paymentsRepository.saveAndFlush(payment);
		});
		return inv;
	}

	@Override
	public String generatePaymentNumber() {
		return paymentsRepository.generatePaymentNumber();
	}

	@Override
	public Payments processByPaymentId(Long id) {
		Payments payment = findExisting(id);
		payment.setStatus("PROCESSED");
		return paymentsRepository.saveAndFlush(payment);
	}

}
