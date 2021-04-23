package com.dominho.order;

public class CartDTO {
	private int cartId;
	private int menuNum;
	private String menuName;
	private String memberId;
	private int quantity;
	private String created;
	private int price;
	public int getCardId() {
		return cartId;
	}
	public void setCardId(int cardId) {
		this.cartId = cardId;
	}
	public int getMenuNum() { 
		return menuNum;
	}
	public void setMenuNum(int menuNum) {
		this.menuNum = menuNum;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
}
