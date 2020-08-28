package com.me.doman;

public class CartItem {
	private Product product;
	private int buyNum;
	private double subtotol;
	public Product getProduct() {
		return product;
	}
	@Override
	public String toString() {
		return "CartItem [product=" + product + ", buyNum=" + buyNum
				+ ", subtotol=" + subtotol + "]";
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public int getBuyNum() {
		return buyNum;
	}
	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}
	public double getSubtotol() {
		return subtotol;
	}
	public void setSubtotol(double subtotol) {
		this.subtotol = subtotol;
	}
}
