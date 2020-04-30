package com.morissoft.printing.payload;

import java.math.BigDecimal;

import javax.validation.constraints.Min;

import com.morissoft.printing.db.ItemPrices;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ItemPricesPayload {
	private Long id;
	@Min(1)
	private Long itemId;
	@Min(1)
	private Integer qtyFrom;
	@Min(1)
	private Integer qtyTo;
	@Min(1)
	private BigDecimal price;

	private String type = "RETAIL";

	public ItemPrices toValueEntity() {
		return new ItemPrices().setItemId(itemId).setPrice(price).setQtyFrom(qtyFrom).setQtyTo(qtyTo).setType(type);
	}

}
