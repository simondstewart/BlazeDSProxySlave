package org.graniteds.tutorial.feed.entities;

import java.io.Serializable;
import java.math.BigDecimal;

public class StockPrice implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal price;
	private String name;

	public StockPrice(String name, BigDecimal price) {
		this.name = name;
		this.price = price;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
