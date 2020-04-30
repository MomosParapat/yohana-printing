package com.morissoft.printing.internal;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.morissoft.printing.event.CustomerSaveCompleteEvent;

@Component
class ApplicationEventListener {
	@EventListener
	public void onEvent(CustomerSaveCompleteEvent event) {
		System.out.println("CUSTOMER_NAME: " + event.getCustomerName());
	}
}
