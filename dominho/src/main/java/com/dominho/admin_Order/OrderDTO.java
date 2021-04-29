package com.dominho.admin_Order;

public class OrderDTO {
	private int listNum;
	private int orderNum;
	private String userId;
	private String storeName;
	private String orderDate;
	private String isDelivery;
	private int totalPrice;
	
	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getIsDelivery() {
		return isDelivery;
	}
	public void setIsDelivery(String isDelivery) {
		this.isDelivery = isDelivery;
	}
	public int getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public int getListNum() {
		return listNum;
	}
	public void setListNum(int listNum) {
		this.listNum = listNum;
	}

	
	

}
