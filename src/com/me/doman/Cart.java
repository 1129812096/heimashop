package com.me.doman;

import java.util.HashMap;
import java.util.Map;

public class Cart {
	private Map<String,CartItem> cartItems=new HashMap<String,CartItem>();
	private double sumTotol;
	public Map<String, CartItem> getCartItems() {
		return cartItems;
	}
	public void setCartItems(Map<String, CartItem> cartItems) {
		this.cartItems = cartItems;
	}
	public double getSumTotol() {
		return sumTotol;
	}
	public void setSumTotol(double sumTotol) {
		this.sumTotol = sumTotol;
	}
}
