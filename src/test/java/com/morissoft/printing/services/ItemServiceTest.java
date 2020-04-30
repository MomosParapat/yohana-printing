package com.morissoft.printing.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.isA;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.morissoft.printing.db.ItemPrices;
import com.morissoft.printing.db.Items;
import com.morissoft.printing.payload.ItemPricesPayload;
import com.morissoft.printing.payload.ItemsPayload;
import com.morissoft.printing.repository.ItemPricesRepository;
import com.morissoft.printing.repository.ItemsRepository;
import com.morissoft.printing.services.bean.ItemServiceBean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class ItemServiceTest {

	@InjectMocks
	private ItemServiceBean itemService;

	@Mock
	private ItemsRepository itemRepository;
	
	@Mock
	private ItemPricesRepository itemPricesRepository;

	private ItemsPayload expectedItem;

	private Items item;
	private Items items =  new Items().setName("Kartu").setCode("I0002");

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		given(itemRepository.save(items)).willAnswer(invocation -> invocation.getArgument(0));
		item = itemService.addItem(items);
		item.setId(1L);
		expectedItem = item.toValueObject();
		log.info("Item after saved {}", expectedItem);

//		ItemPrices itemPrices_1 = ItemPrices.builder().itemId(expectedItem.getId()).price(BigDecimal.valueOf(20000)).qtyFrom(1)
//				.qtyTo(6).build();
//		given(itemPricesRepository.save(itemPrices_1)).willReturn(itemPrices_1);
//		//when(itemPricesRepository.save(itemPrices_1)).thenReturn(itemPrices_1);
//		itemPrices_1 = itemService.addItemPrice(itemPrices_1);
//		itemPrices_1.setId(1L);

//		ItemPrices itemPrices_2 = ItemPrices.builder().itemId(expectedItem.getId()).price(BigDecimal.valueOf(18000)).qtyFrom(7)
//				.qtyTo(15).build();
//		given(itemPricesRepository.save(itemPrices_2)).willAnswer(invocation -> invocation.getArgument(0));
//		itemPrices_2 = itemService.addItemPrice(itemPrices_2);
//
//		ItemPrices itemPrices_3 = ItemPrices.builder().itemId(expectedItem.getId()).price(BigDecimal.valueOf(17000)).qtyFrom(16)
//				.qtyTo(1000).build();
//		given(itemPricesRepository.save(itemPrices_3)).willAnswer(invocation -> invocation.getArgument(0));
//		itemPrices_3 = itemService.addItemPrice(itemPrices_3);

	}

	@Test
	public void WhenFindByItemId_thenReturnItemPriceList() {
		List<ItemPrices> itemPriceList = new ArrayList<ItemPrices>();
		ItemPrices itemPrices_1 = new ItemPrices()
				.setItemId(expectedItem.getId())
				.setPrice(BigDecimal.valueOf(20000))
				.setQtyFrom(1)
				.setQtyTo(6);
		ItemPrices itemPrices_2 = new ItemPrices()
				.setItemId(expectedItem.getId())
				.setPrice(BigDecimal.valueOf(18000))
				.setQtyFrom(7)
				.setQtyTo(15);

		ItemPrices itemPrices_3 = new ItemPrices()
				.setItemId(expectedItem.getId())
				.setPrice(BigDecimal.valueOf(17000))
				.setQtyFrom(16)
				.setQtyTo(1000);
		itemPriceList.add(itemPrices_1);
		itemPriceList.add(itemPrices_2);
		itemPriceList.add(itemPrices_3);

		when(itemPricesRepository.findByItemId(expectedItem.getId())).thenReturn(itemPriceList);

		List<ItemPrices> findItemPriceList = itemService.findItemPricesByItemId(expectedItem.getId());

		assertEquals(3, findItemPriceList.size());
		verify(itemPricesRepository, times(1)).findByItemId(expectedItem.getId());
	}

	@Test(expected = Exception.class)
	public void WhenSaveExistingItemPrice_thenThrowException() throws Exception {
		List<ItemPrices> itemPriceList = new ArrayList<ItemPrices>();
		ItemPrices itemPrices_1 = new ItemPrices()
				.setItemId(expectedItem.getId())
				.setPrice(BigDecimal.valueOf(20000))
				.setQtyFrom(1)
				.setQtyTo(6);
		itemPrices_1.setId(1L);
		ItemPrices itemPrices_2 = new ItemPrices()
				.setItemId(expectedItem.getId())
				.setPrice(BigDecimal.valueOf(18000))
				.setQtyFrom(7)
				.setQtyTo(15);
		itemPrices_2.setId(2L);

		ItemPrices itemPrices_3 = new ItemPrices()
				.setItemId(expectedItem.getId())
				.setPrice(BigDecimal.valueOf(17000))
				.setQtyFrom(16)
				.setQtyTo(100);
		itemPrices_3.setId(3L);

		itemPriceList.add(itemPrices_1);
		itemPriceList.add(itemPrices_2);
		itemPriceList.add(itemPrices_3);

		ItemPrices itemPrices_4 = new ItemPrices()
				.setItemId(expectedItem.getId())
				.setPrice(BigDecimal.valueOf(20000))
				.setQtyFrom(101)
				.setQtyTo(200);;

//		given(itemPricesRepository.findByItemId(expectedItem.getId())).willReturn(itemPriceList);
		given(itemRepository.existsById(expectedItem.getId())).willReturn(true);
		boolean exist = itemPricesRepository.isPriceOverLapping(expectedItem.getId(), itemPrices_4.getPrice(),itemPrices_4.getType());
		log.info("is Price Overlapping? {}", exist);
		given(itemPricesRepository.isPriceOverLapping(expectedItem.getId(), itemPrices_4.getPrice(),itemPrices_4.getType())).willReturn(true);
//		given(itemPricesRepository.isQuantityOverLapping(expectedItem.getId(), 15)).willReturn(true);
		ItemPricesPayload payLoad=new ItemPricesPayload()
				.setItemId(expectedItem.getId())
				.setPrice(itemPrices_4.getPrice())
				.setQtyFrom(itemPrices_4.getQtyFrom())
				.setQtyTo(itemPrices_4.getQtyTo());


		ItemPrices itemPrices = itemService.addItemPrice(payLoad);

//		given(itemPricesRepository.isPriceOverLapping(expectedItem.getId(), BigDecimal.valueOf(20000)))
//				.willReturn(true);
//		assertThrows(Exception.class, () -> {
//			itemService.addItemPrice(itemPrices_4);
//		});
		verify(itemPricesRepository, never()).save(any(ItemPrices.class));

	}

	@Test
	public void WhenAddNewSameItem_thenReturnNull() throws Exception {
		assertThat(item).isNotNull();
		verify(itemRepository).save(any(Items.class));
		verify(itemRepository, times(1)).save(items);
	}

	@Test
	public void WhenSaveExistingItem_thenThrowException() throws Exception {
		when(itemRepository.save(items)).thenReturn(items);
		itemService.addItem(items);
	}

}