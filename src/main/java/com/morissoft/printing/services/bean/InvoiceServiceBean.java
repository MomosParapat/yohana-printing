package com.morissoft.printing.services.bean;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.morissoft.printing.db.Payments;
import com.morissoft.printing.db.SalesInvoice;
import com.morissoft.printing.db.SalesOrder;
import com.morissoft.printing.payload.SalesInvoicePayload;
import com.morissoft.printing.payload.SalesOrderPayload;
import com.morissoft.printing.repository.SalesInvoiceRepository;
import com.morissoft.printing.services.CustomerService;
import com.morissoft.printing.services.InvoiceService;
import com.morissoft.printing.services.PaymentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Component
@Transactional
public class InvoiceServiceBean implements InvoiceService {

	@Autowired
	private SalesInvoiceRepository salesInvoiceRepository;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private PaymentService paymentService;

	@Override
	public SalesInvoice save(SalesOrderPayload command) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SalesInvoice> findAll() {
		// TODO Auto-generated method stub
		return salesInvoiceRepository.findAll();
	}

	@Override
	public SalesInvoice findById(Long id) {
		Optional<SalesInvoice> findById = salesInvoiceRepository.findById(id);
		if (findById.isPresent()) {
			return findById.get();
		}
		return null;
	}

	@Override
	public List<SalesInvoicePayload> findAllSalesOrder() {
		// TODO Auto-generated method stub
		return salesInvoiceRepository.findAll().stream().map(x -> x.toValueObject()).collect(Collectors.toList());
	}

	@Override
	public SalesInvoice findByOrderNumber(String orderNumber) throws Exception {
		Optional<SalesInvoice> optSo = salesInvoiceRepository.findByOrderNumber(orderNumber);
		if (!optSo.isPresent()) {
			throw new Exception("invoice with number " + orderNumber + " does not exist");
		}
		return optSo.get();
	}
	
	@Override
	public SalesInvoice delete(Long id) {
		Optional<SalesInvoice> findById = salesInvoiceRepository.findById(id);
		if (findById.isPresent()) {
			SalesInvoice so = findById.get();
			so.setStatus("DELETED");
			return salesInvoiceRepository.saveAndFlush(so);
		}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SalesInvoice> findSalesReportByPeriod(String dateFrom, String dateTo) {
		// TODO Auto-generated method stub
		return salesInvoiceRepository.findByPeriod(dateFrom, dateTo);
	}

	@Override
	public SalesInvoice deliver(Long id) {
		return paymentService.processByInvoice(id);
	}

//	@Override
//	public SalesInvoicePayload findByActiveStatus(Long invoiceId) {
//		return salesInvoiceRepository.findByStatus("ACTIVE").stream().map(SalesInvoice::toValueObject)
//				.collect(Collectors.toList());
//	}

	@Override
	public SalesInvoice findByActiveStatus(SalesOrder order) {
		Optional<SalesInvoice> inv = salesInvoiceRepository.findByOrderAndStatus(order, "ACTIVE");
		if (inv.isPresent()) {
			return inv.get();
		}
		return null;
	}

}
