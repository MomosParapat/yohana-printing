package com.morissoft.printing.services.bean;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.morissoft.printing.db.Categories;
import com.morissoft.printing.db.ItemPrices;
import com.morissoft.printing.db.Items;
import com.morissoft.printing.db.SalesOrder;
import com.morissoft.printing.payload.ItemPricesPayload;
import com.morissoft.printing.payload.ItemsPayload;
import com.morissoft.printing.repository.CategoriesRepository;
import com.morissoft.printing.repository.ItemPricesRepository;
import com.morissoft.printing.repository.ItemsRepository;
import com.morissoft.printing.services.ItemService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Component
@Transactional
public class ItemServiceBean implements ItemService {

	@Autowired
	private ItemsRepository itemsRepository;

	@Autowired
	private ItemPricesRepository itemPricesRepository;

	@Autowired
	private CategoriesRepository categoriesRepository;

	@Override
	public Items addItem(Items items) throws Exception {
		Optional<Items> itOptional = itemsRepository.findById(items.getId());
		Items itemsEntity = items;
		if (!itOptional.isPresent()) {
			log.info("Adding new Items [{}]", items);
			if (itemsRepository.existByName(items.getName()))
				throw new Exception("name :" + items.getName() + " is already exists");
			if (itemsRepository.existByCode(items.getCode()))
				throw new Exception("code :" + items.getCode() + " is already exists");
			return itemsRepository.save(itemsEntity);
		} else {
			log.info("Updating Item [{}]", items);
			return itemsRepository.saveAndFlush(itemsEntity);
		}
	}

	public ItemPrices addItemPrice(ItemPricesPayload itemPrices) throws Exception {
		log.info("Adding new Item Price [{}]", itemPrices);

		if (!itemsRepository.existsById(itemPrices.getItemId()))
			throw new Exception("item :" + itemPrices.getItemId() + " does not exist.");

		if (itemPricesRepository.isPriceOverLapping(itemPrices.getItemId(), itemPrices.getPrice(),
				itemPrices.getType())) {
			log.info("Item Price [{}]", itemPrices.getPrice());
			throw new Exception("price :" + itemPrices.getPrice() + " is over lapping.");
		}

		if (itemPricesRepository.isQuantityOverLapping(itemPrices.getItemId(), itemPrices.getQtyFrom(),
				itemPrices.getType())) {
			log.info("Item Qty From [{}]", itemPrices.getQtyFrom());
			throw new Exception("quantity range :" + itemPrices.getQtyFrom() + " is over lapping.");
		}

		if (itemPricesRepository.isQuantityOverLapping(itemPrices.getItemId(), itemPrices.getQtyTo(),
				itemPrices.getType())) {
			log.info("Item Qty To [{}]", itemPrices.getQtyFrom());
			throw new Exception("quantity range :" + itemPrices.getQtyTo() + " is over lapping.");
		}
		return itemPricesRepository.save(itemPrices.toValueEntity());
	}

	@Override
	public List<ItemPrices> findItemPricesByItemId(Long itemId) {
		return itemPricesRepository.findByItemId(itemId);
	}

	@Override
	public List<Items> findAllItems() {
		return itemsRepository.findAll();
	}

	@Override
	public Items save(ItemsPayload command) throws Exception {
		Optional<Items> itOptional = itemsRepository.findById(command.getId());
		Categories optCategory = categoriesRepository.findById(command.getCategories().getId()).get();
		Items itemsEntity;
		if (!itOptional.isPresent()) {
			log.info("Adding new Items [{}]", command);
			itemsEntity = new Items().setCode(command.getCode()).setStatus(command.getStatus())
					.setName(command.getName()).setCategories(optCategory);
			if (itemsRepository.existByName(command.getName()))
				throw new Exception("name :" + command.getName() + " is already exists");
			if (itemsRepository.existByCode(command.getCode()))
				throw new Exception("code :" + command.getCode() + " is already exists");
			return itemsRepository.save(itemsEntity);
		} else {
			log.info("Updating Item [{}]", command);
			itemsEntity = itOptional.get();
			return itemsRepository.saveAndFlush(itemsEntity);
		}
	}

	@Override
	public Items findById(Long id) {
		// TODO Auto-generated method stub
		return itemsRepository.findById(id).get();
	}

	@Override
	public ItemPrices findItemPriceById(Long id) {
		// TODO Auto-generated method stub
		return itemPricesRepository.findById(id).get();
	}

	@Override
	public ItemPrices findItemPriceByQtyInBetween(Long id, Integer qty, String type) throws Exception {
		// TODO Auto-generated method stub
		Optional<ItemPrices> optPrice = itemPricesRepository.findPriceByQtyInBetween(id, qty, type);
		if (optPrice.isPresent()) {
			return optPrice.get();
		} else {
			throw new Exception(
					"the prices is not set up yet for item: " + itemsRepository.findById(id).get().getName());
		}
	}

	@Override
	public List<Categories> findAllCategories() {
		// TODO Auto-generated method stub
		return categoriesRepository.findAll();
	}

	@Override
	public Items delete(Long id) {
		Optional<Items> findById = itemsRepository.findById(id);
		if (findById.isPresent()) {
			Items items = findById.get();
			items.setStatus("DELETED");
			return itemsRepository.saveAndFlush(items);
		}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Items findByCode(String code) throws Exception {
		Optional<Items> optSo = itemsRepository.findByCode(code);
		if (!optSo.isPresent()) {
			throw new Exception("Item with code: " + code + " does not exist");
		}
		return optSo.get();
	}

	@Override
	public List<Items> findActiveItems() {
		// TODO Auto-generated method stub
		return itemsRepository.findByStatusOrderByName("ACTIVE");
	}

}
