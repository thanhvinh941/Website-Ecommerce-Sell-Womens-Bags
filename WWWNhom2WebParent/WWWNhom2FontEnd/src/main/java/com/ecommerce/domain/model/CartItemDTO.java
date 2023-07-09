package com.ecommerce.domain.model;

import lombok.Data;

@Data
public class CartItemDTO {
	private	Integer id;
	private String name;
	private String image;
	private String alias;
	private long price;
	private long discountPercent;
	private long discountPrice;
	private int quantity;
}
