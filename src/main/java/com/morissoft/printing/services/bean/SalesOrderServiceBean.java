package com.morissoft.printing.services.bean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.morissoft.printing.db.Customer;
import com.morissoft.printing.db.Items;
import com.morissoft.printing.db.SalesOrder;
import com.morissoft.printing.db.SalesOrderDetails;
import com.morissoft.printing.payload.SalesOrderDetailsPayload;
import com.morissoft.printing.payload.SalesOrderPayload;
import com.morissoft.printing.repository.SalesOrderDetailsRepository;
import com.morissoft.printing.repository.SalesOrderRepository;
import com.morissoft.printing.services.CustomerService;
import com.morissoft.printing.services.ItemService;
import com.morissoft.printing.services.SalesOrderService;

import org.springframework.beans.BeanUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Component
@Transactional
public class SalesOrderServiceBean implements SalesOrderService {

	@Autowired
	private SalesOrderRepository salesOrderRepository;

	@Autowired
	private SalesOrderDetailsRepository salesOrderDetailsRepository;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private ItemService itemService;

	@Override
	public SalesOrder save(SalesOrderPayload command) throws Exception {
		SalesOrder findById = findById(command.getId());
		SalesOrder so;
		Customer cust = customerService.findById(command.getCustomerId());
		if (findById != null) {
			command.setTotalOutstanding(command.getPaymentNet().subtract(command.getTotalPaid()));
			SalesOrder salesOrder = findById;
			salesOrder.setCustomer(cust);
			salesOrder.setDiscount(command.getDiscount());
			salesOrder.setOrderNumber(command.getOrderNumber());
			salesOrder.setPaymentNet(command.getPaymentNet());
			salesOrder.setStatus(command.getStatus());
			salesOrder.setSubTotal(command.getSubTotal());
			salesOrder.setCompletedDate(command.toValueEntity().getCompletedDate());
			log.info("Updated SalesOrder {}", salesOrder);
			so = salesOrderRepository.saveAndFlush(salesOrder);

		} else {
//			String soNumber = generateOrderNumber();
//			command.setOrderNumber(soNumber);
			log.info("New SalesOrder {}", command);
			so = salesOrderRepository.save(command.toValueEntity().setCustomer(cust));
		}
		updateTotal(new SalesOrderDetails().setOrderId(so.getId()));
		return so;
	}

	@Override
	public SalesOrderDetails saveOrderDetails(SalesOrderDetailsPayload command) throws Exception {
		log.info("Details {}", command);
		Optional<SalesOrderDetails> saOptional = salesOrderDetailsRepository.findById(command.getId());

		SalesOrderDetails detail;
		Items item = itemService.findById(command.getItems().getId());
		if (saOptional.isPresent()) {
			detail = salesOrderDetailsRepository.saveAndFlush(toEntityEdit(command, saOptional.get()).setItems(item));
		} else {
			detail = salesOrderDetailsRepository.save(command.toValueEntity().setItems(item));
		}
		updateTotal(detail);
		return detail;
	}

	SalesOrderDetails toEntityEdit(SalesOrderDetailsPayload form, SalesOrderDetails detail) {
		BeanUtils.copyProperties(form, detail);
		return detail;
	}

	void updateTotal(SalesOrderDetails detail) {
		List<SalesOrderDetails> details = salesOrderDetailsRepository.findByOrderId(detail.getOrderId());
		BigDecimal total = BigDecimal.ZERO;
		for (SalesOrderDetails dt : details) {
			total = total.add(dt.getLineTotal());
		}
		SalesOrder order = salesOrderRepository.findById(detail.getOrderId()).get();
		order.setSubTotal(total);
		order.setPaymentNet(order.getSubTotal().subtract(order.getDiscount()));
		order.setTotalOutstanding(order.getPaymentNet());
		salesOrderRepository.saveAndFlush(order);
	}

	@Override
	public List<SalesOrder> findAll() {
		// TODO Auto-generated method stub
		return salesOrderRepository.findAll();
	}

	@Override
	public List<SalesOrderDetails> findOrderDetailsByOrderId(Long orderId) {
		// TODO Auto-generated method stub
		return salesOrderDetailsRepository.findByOrderId(orderId);
	}

	@Override
	public SalesOrder findById(Long id) {
		Optional<SalesOrder> so = salesOrderRepository.findById(id);
		if (so.isPresent())
			return so.get();
		return null;
	}

	@Override
	public SalesOrderDetails findOrderDetailsById(Long id) {
		// TODO Auto-generated method stub
		return salesOrderDetailsRepository.findById(id).get();
	}

	@Override
	public List<SalesOrderPayload> findAllSalesOrder() {
		// TODO Auto-generated method stub
		return salesOrderRepository.findAllSalesOrder().stream().map(x -> x.toValueObject())
				.collect(Collectors.toList());
	}

	@Override
	public SalesOrder findByOrderNumber(String orderNumber) throws Exception {
		SalesOrder optSo = findByNumber(orderNumber);
		if (optSo == null) {
			throw new Exception("sales order with number " + orderNumber + " does not exist");
		}
		return optSo;
	}

	@Override
	public SalesOrder delete(Long id) {
		Optional<SalesOrder> findById = salesOrderRepository.findById(id);
		if (findById.isPresent()) {
			SalesOrder so = findById.get();
			so.setStatus("DELETED");
			return salesOrderRepository.saveAndFlush(so);
		}
		// TODO Auto-generated method stub
		return null;
	}

	private SalesOrder findByNumber(String orderNumber) {
		Optional<SalesOrder> optSo = salesOrderRepository.findByOrderNumber(orderNumber);
		if (optSo.isPresent()) {
			return optSo.get();
		}
		return null;
	}

	@Override
	public boolean existByNumber(String orderNumber) {
		return findByNumber(orderNumber) != null;
	}

	@Override
	public String generateOrderNumber() {
		// TODO Auto-generated method stub
		return salesOrderRepository.generateOrderNumber();
	}

}
