package com.morissoft.printing.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.morissoft.printing.db.ItemPrices;
import com.morissoft.printing.db.Items;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ItemsJpaTest {
//	@Autowired
//	private TestEntityManager testEntityManager;
//
//	@Autowired
//	private ItemsRepository itemsRepository;
//
//	@Autowired
//	private ItemPricesRepository itemPricesRepository;
//
//	Items itemsData;
//
//	@Before
//	public void setUp() {
//		Items items = new Items().setName("Kartu Name").setCode("I0001");
//		itemsData = testEntityManager.persist(items);
//
//		ItemPrices itemPrices = new ItemPrices()
//				.setItemId(itemsData.getId())
//				.setPrice(BigDecimal.valueOf(20000))
//				.setQtyFrom(1)
//				.setQtyTo(6);
//
//		testEntityManager.persist(itemPrices);
//		itemPrices = new ItemPrices()
//				.setItemId(itemsData.getId())
//				.setPrice(BigDecimal.valueOf(18000))
//				.setQtyFrom(7)
//				.setQtyTo(15);
//
//		testEntityManager.persist(itemPrices);
//		itemPrices = new ItemPrices()
//				.setItemId(itemsData.getId())
//				.setPrice(BigDecimal.valueOf(17000))
//				.setQtyFrom(15)
//				.setQtyTo(1000);		
//		testEntityManager.persist(itemPrices);
//	}
//
//	@Test
//	public void WhenIsExistByName_thenReturnTrue() {
//		boolean exist = itemsRepository.existByName("Kartu Nama");
//		assertThat(exist).isEqualTo(true);
//	}
//
//	@Test
//	public void WhenIsNotExistByName_thenReturnFalse() {
//		boolean exist = itemsRepository.existByName("Kartu");
//		assertThat(exist).isEqualTo(false);
//	}
//
//	@Test
//	public void WhenIsExistByCode_thenReturnTrue() {
//		boolean exist = itemsRepository.existByCode("I0001");
//		assertThat(exist).isEqualTo(true);
//	}
//
//	@Test
//	public void WhenIsNotExistByCode_thenReturnFalse() {
//		boolean exist = itemsRepository.existByCode("I000");
//		assertThat(exist).isEqualTo(false);
//	}
//
//	@Test
//	public void WhenFindById_thenReturnItem() {
//		Items findItem = itemsRepository.findById(itemsData.getId()).get();
//		assertThat(findItem.getName()).isEqualTo("Kartu Nama");
//		assertThat(findItem.getCode()).isEqualTo("I0001");
//	}
//
//	@Test
//	public void WhenFindByItemId_thenReturnItemPricesList() {
//		List<ItemPrices> itemPricesList = itemPricesRepository.findByItemId(itemsData.getId());
//		itemPricesList.forEach(price -> log.info("items [{}]", price));
//		assertThat(itemPricesList.size()).isEqualTo(3);
//	}
//
//	@Test
//	public void WhenFindPriceByQtyInBetween_thenReturnItemPrices() {
//		ItemPrices itemOptional_1 = itemPricesRepository.findPriceByQtyInBetween(itemsData.getId(), 5).get();
//		assertThat(itemOptional_1.getPrice()).isEqualTo(BigDecimal.valueOf(20000));
//
//		ItemPrices itemOptional_2 = itemPricesRepository.findPriceByQtyInBetween(itemsData.getId(), 7).get();
//		assertThat(itemOptional_2.getPrice()).isEqualTo(BigDecimal.valueOf(18000));
//
//		ItemPrices itemOptional_3 = itemPricesRepository.findPriceByQtyInBetween(itemsData.getId(), 1000).get();
//		assertThat(itemOptional_3.getPrice()).isEqualTo(BigDecimal.valueOf(17000));
//	}
//
//	@Test
//	public void WhenIsPriceOverLapping_thenReturnTrue() {
//		boolean exist = itemPricesRepository.isPriceOverLapping(itemsData.getId(), BigDecimal.valueOf(20000));
//		assertThat(exist).isEqualTo(true);
//	}
//
//	@Test
//	public void WhenIsPriceNotOverLapping_thenReturnFalse() {
//		boolean exist = itemPricesRepository.isPriceOverLapping(itemsData.getId(), BigDecimal.valueOf(25000));
//		assertThat(exist).isEqualTo(false);
//	}
//
//	@Test
//
//	public void WhenIsQuantityOverLapping_thenReturnTrue() {
//		boolean exist = itemPricesRepository.isQuantityOverLapping(itemsData.getId(), 99);
//		assertThat(exist).isEqualTo(true);
//	}
//
//	@Test
//	public void WhenIsQuantityNotOverLapping_thenReturnFalse() {
//		boolean exist = itemPricesRepository.isQuantityOverLapping(itemsData.getId(), 1002);
//		assertThat(exist).isEqualTo(false);
//	}
}
