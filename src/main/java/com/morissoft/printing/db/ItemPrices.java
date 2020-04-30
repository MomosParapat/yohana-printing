package com.morissoft.printing.db;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.morissoft.printing.base.BaseEntity;
import com.morissoft.printing.payload.ItemPricesPayload;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "items_prices", schema = "yohana_printing")
public class ItemPrices extends BaseEntity {
	@Column(nullable = false)
	private Long itemId;
	@Column(nullable = false)
	private String type;
	@Column(nullable = false)
	private Integer qtyFrom;
	@Column(nullable = false)
	private Integer qtyTo;
	@Column(nullable = false)
	private BigDecimal price;

	public ItemPricesPayload toValueObject() {
		return new ItemPricesPayload().setId(this.getId()).setItemId(this.itemId).setPrice(this.price).setQtyFrom(this.qtyFrom)
				.setQtyTo(this.qtyTo).setType(type);
	}

}
