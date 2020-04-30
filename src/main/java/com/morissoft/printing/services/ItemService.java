package com.morissoft.printing.services;

import java.util.List;

import com.morissoft.printing.db.Categories;
import com.morissoft.printing.db.ItemPrices;
import com.morissoft.printing.db.Items;
import com.morissoft.printing.payload.ItemPricesPayload;
import com.morissoft.printing.payload.ItemsPayload;

public interface ItemService {
	Items addItem(Items items) throws Exception;

	ItemPrices addItemPrice(ItemPricesPayload itemPrices) throws Exception;

	List<Items> findAllItems();

	List<ItemPrices> findItemPricesByItemId(Long itemId);

	Items save(ItemsPayload command) throws Exception;

	Items findById(Long id);

	ItemPrices findItemPriceById(Long id);

	ItemPrices findItemPriceByQtyInBetween(Long id, Integer qty, String type) throws Exception;

	List<Categories> findAllCategories();

	Items delete(Long id);

	Items findByCode(String code) throws Exception;

	List<Items> findActiveItems();

}
