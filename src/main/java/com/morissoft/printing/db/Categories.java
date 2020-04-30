package com.morissoft.printing.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.morissoft.printing.base.BaseEntity;
import com.morissoft.printing.payload.CategoriesPayload;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "categories", schema = "yohana_printing")
public class Categories extends BaseEntity {

	@Column(nullable = false)
	private String name;
	
	public CategoriesPayload toValueObject() {
		return new CategoriesPayload()
				.setId(this.getId())
				.setName(this.name);
	}
}
