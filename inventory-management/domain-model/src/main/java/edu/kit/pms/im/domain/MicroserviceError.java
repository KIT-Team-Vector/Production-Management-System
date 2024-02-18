package edu.kit.pms.im.domain;

public class MicroserviceError extends Error {
	
	private static final long serialVersionUID = 1L;

	private String type;

	private int orderID;

	private String message;
	
	public MicroserviceError(String type, String message, int orderID) {
		this.type = type;
		this.message = message;
		this.orderID = orderID;
	}
	
	public MicroserviceError(String type, String message) {
		this.type = type;
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

	public int getOrderID() {
		return orderID;
	}
	
	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}
	
	public String getType() {
		return type;
	}
}
