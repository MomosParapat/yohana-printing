package com.morissoft.printing.db;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import com.morissoft.printing.base.BaseEntity;
import com.morissoft.printing.payload.ItemsPayload;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@Entity
@Table(name = "items", schema = "yohana_printing")
public class Items extends BaseEntity {
	private String code;
	private String name;
	private String status;
	
	@ManyToOne
	@JoinColumn(name = "categoryId", nullable = false)
	private Categories categories;
	
	public ItemsPayload toValueObject() {
		return new ItemsPayload().setId(this.getId()).setCode(this.code).setName(this.name).setStatus(this.status).setCategories(this.categories.toValueObject());
	}
}
