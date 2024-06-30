package main.java.orderbook;

import com.fasterxml.jackson.annotation.JsonProperty;

//class to store TIp data as an object
//abstract, extended by Ask and Bid
abstract class Tip {
	
	@JsonProperty("price")
	private String price;
	
	@JsonProperty("qty")
	private String quantity;
	
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
}
