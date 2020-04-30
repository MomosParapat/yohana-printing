package com.morissoft.printing.repository;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.morissoft.printing.db.Categories;
import com.morissoft.printing.db.Customer;
import com.morissoft.printing.db.ItemPrices;
import com.morissoft.printing.db.Items;
import com.morissoft.printing.db.SalesOrder;
import com.morissoft.printing.db.SalesOrderDetails;
import com.morissoft.printing.payload.ItemPricesPayload;
import com.morissoft.printing.payload.ItemsPayload;

import org.junit.Before;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class SalesOrderJpaTest {
//	@Autowired
//	private TestEntityManager testEntityManager;
//
//	@Autowired
//	private SalesOrderRepository salesOrderRepository;
//
//	@Autowired
//	private SalesOrderDetailsRepository salesOrderDetailsRepository;
//
//	@Autowired
//	private ItemPricesRepository itemPricesRepository;
//
//	List<ItemPrices> itemPricesList;
//	ItemsPayload item;
//
//	@Before
//	public void setUp() {
//		Customer cust = addCustomer();
//		item = addItem();
//		SalesOrder salesOrder = new SalesOrder();
//		salesOrder.setCustomer(cust);
//		salesOrder.setOrderNumber("SO00001");
//		salesOrder.setSubTotal(BigDecimal.valueOf(500000));
//		salesOrder.setDiscount(BigDecimal.valueOf(0));
//		salesOrder.setPaymentNet(BigDecimal.valueOf(500000));
//		salesOrder.setStatus("NEW");
//
//		testEntityManager.persist(salesOrder);
//
//		Integer qty = 25;
//		BigDecimal lineDisc = BigDecimal.valueOf(500);
//
//		ItemPrices itemPrices = itemPricesRepository.findPriceByQtyInBetween(item.getId(), qty).get();
//		SalesOrderDetails salesOrderDetails = new SalesOrderDetails().setItems(item.toValueEntity())
//				.setOrderId(salesOrder.getId()).setQty(qty).setPrice(itemPrices.getPrice()).setLineDisc(lineDisc)
//				.setSubTotal(itemPrices.getPrice().multiply(BigDecimal.valueOf(qty)))
//				.setLineTotal(itemPrices.getPrice().multiply(BigDecimal.valueOf(qty)).subtract(lineDisc));
//		testEntityManager.persist(salesOrderDetails);
//	}
//
//	Customer addCustomer() {
//		Customer customer = new Customer().setFirstName("Momos").setLastName("Parapat").setAddress("Bekasi")
//				.setEmail("iphonp@yahoo.com").setPhoneNumber("0812809903366").setStatus("NEW");
//		return testEntityManager.persist(customer);
//	}
//
//	ItemsPayload addItem() {
//		Categories cat = new Categories().setName("Indoor");
//		cat.setId(1L);;
//		Categories res = testEntityManager.persist(cat);
//
//		Items items = new Items().setName("Kartu Name").setCode("I0001").setCategories(res);
//		Items itemsData = testEntityManager.persist(items);
//
//		ItemPrices itemPrices = new ItemPrices().setItemId(itemsData.getId()).setPrice(BigDecimal.valueOf(20000))
//				.setQtyFrom(1).setQtyTo(6);
//		testEntityManager.persist(itemPrices);
//
//		itemPrices = new ItemPrices().setItemId(itemsData.getId()).setPrice(BigDecimal.valueOf(18000)).setQtyFrom(7)
//				.setQtyTo(15);
//		testEntityManager.persist(itemPrices);
//
//		itemPrices = new ItemPrices().setItemId(itemsData.getId()).setPrice(BigDecimal.valueOf(17000)).setQtyFrom(15)
//				.setQtyTo(1000);
//		testEntityManager.persist(itemPrices);
//
//		itemPricesList = itemPricesRepository.findByItemId(itemsData.getId());
//		itemPricesList.forEach(price -> log.info("FindByItemId on Sales Order [{}]", price));
//
//		return itemsData.toValueObject();
//
//	}
//
//	@Test
//	public void WhenFindById_thenReturnSalesOrder() {
//		List<SalesOrderDetails> salesOrderDetails = salesOrderDetailsRepository.findByOrderId(item.getId());
//		assertThat(salesOrderRepository.count()).isEqualTo(1);
//		assertThat(salesOrderDetails.size()).isEqualTo(1);
//	}

}
