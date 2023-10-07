package com.cozyhome.onlineshop.model.enums;

import lombok.Getter;

@Getter
public enum ProductQuantityStatus {
	IN_STOCK("В наявності", true),
	OUT_OF_STOCK("Немає на складі", false),
	LIMITED_STOCK("Закінчується", true);

	private final String description;
    private final boolean isAvailable;

	ProductQuantityStatus(String description, boolean isAvailable){
		this.description = description;
        this.isAvailable = isAvailable;
	}

	public static ProductQuantityStatus getByProductQuantity(int productQuantity) {
		final int zero_quantity = 0;
		final int limited_quantity = 2;

        if (productQuantity == zero_quantity) {
            return OUT_OF_STOCK;
        } else if (productQuantity < limited_quantity) {
            return LIMITED_STOCK;
        } else {
            return IN_STOCK;
        }
    }
}