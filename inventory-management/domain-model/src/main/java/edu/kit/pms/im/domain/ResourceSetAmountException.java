package edu.kit.pms.im.domain;

public class ResourceSetAmountException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;

	private int deltaAmount;

	private int amountInInventory;

	public ResourceSetAmountException(int id, int deltaAmount, int amountInInventory) {
		super("The amount of a resource cannot fall below zero");
		this.id = id;
		this.deltaAmount = deltaAmount;
		this.amountInInventory = amountInInventory;
	}
	
	public int getId() {
		return id;
	}

	public int getDeltaAmount() {
		return deltaAmount;
	}

	public int getAmountInInventory() {
		return amountInInventory;
	}
}