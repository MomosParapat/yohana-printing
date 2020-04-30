package com.morissoft.printing.event;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CustomerSaveCompleteEvent {
	private String customerName;
}
