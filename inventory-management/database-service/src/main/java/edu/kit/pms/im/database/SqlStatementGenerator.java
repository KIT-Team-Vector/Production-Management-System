package edu.kit.pms.im.database;

public class SqlStatementGenerator {
	
	public static String selectAllResourceSets() {
		return "SELECT * FROM inventory";
	}
	
	public static String selectResourceSetWithId() {
		return "SELECT * FROM inventory WHERE id = ?";
	}
	
	public static String selectAndLockAmountWithId() {
		return "SELECT amount FROM inventory WHERE id = ? FOR UPDATE";
	}
	
	public static String selectResourceSetWithName() {
		return "SELECT * FROM inventory WHERE primary_name = ?";
	}
	
	public static String deleteResourceSetWithId() {
		return "DELETE * FROM inventory WHERE id = ?";
	}
	
	public static String updateResourceSetWithAmount() {
		return "UPDATE inventory SET amount = ? WHERE id = ?";
	}
	

}
