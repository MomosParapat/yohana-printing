package com.morissoft.printing.command;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SaveItemCommand {
	private Long id;
	private String code;
	private String name;
}
