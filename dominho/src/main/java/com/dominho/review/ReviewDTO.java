package com.dominho.review;

public class ReviewDTO {
	private String ReviewId, content;
	private int orderNum;
	
	public String getReviewId() {
		return ReviewId;
	}
	public void setReviewId(String reviewId) {
		ReviewId = reviewId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}	
}
