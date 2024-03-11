package edu.kit.pms.im.domain;


public class InventoryManagementError extends Error {
	
	private static final long serialVersionUID = -9121677943196379445L;

	private String type;

	private int orderID;

	private String message;
	
	public InventoryManagementError(String type, String message, int orderID) {
		this.type = type;
		this.message = message;
		this.orderID = orderID;
	}
	
	public InventoryManagementError(String type, String message) {
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
