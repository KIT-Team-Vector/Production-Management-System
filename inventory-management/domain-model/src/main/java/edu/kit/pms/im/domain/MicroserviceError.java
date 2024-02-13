package edu.kit.pms.im.domain;

public class MicroserviceError {

	private int orderID;

	private String message;
	
	public MicroserviceError(String message, int orderID) {
		this.message = message;
		this.orderID = orderID;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getOrderID() {
		return orderID;
	}

}
