package com.morissoft.printing.payload;

import javax.persistence.Column;

import com.morissoft.printing.db.Categories;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CategoriesPayload {
	private Long id;
	@Column(nullable = false)
	private String name;

	public Categories toValueEntity() {
		return new Categories().setName(name);
	}
}
