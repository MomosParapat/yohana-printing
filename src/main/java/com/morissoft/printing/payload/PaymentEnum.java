package com.morissoft.printing.payload;

public enum PaymentEnum {
	CASH("CASH"), BANK("BANK"), OTHERS("OTHERS");

	private final String type;

	PaymentEnum(String userType) {

		this.type = userType;

	}

	public String getType() {

		return this.type;
	}
}