package com.morissoft.printing.payload;

import com.morissoft.printing.db.Items;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ItemsPayload {
	private Long id;
	private String code;
	private String name;
	private String status = "ACTIVE";

	private CategoriesPayload categories;

	public Items toValueEntity() {
		return new Items().setStatus(status).setCode(code).setName(name).setCategories(categories.toValueEntity());

	}

//	List<ItemPricesPayload> itemPrices;
}
