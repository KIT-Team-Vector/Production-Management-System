package edu.kit.pms.im.database;

public class SqlStatementGenerator {

	public static String selectAllResourceSets() {
		return "SELECT * FROM inventory";
	}

	public static String selectResourceSetWithId() {
		return "SELECT * FROM inventory WHERE id = ?";
	}

	public static String selectAndLockResourceSetWithId() {
		return "SELECT amount FROM inventory WHERE id = ? FOR UPDATE";
	}

	public static String selectResourceSetWithName() {
		return "SELECT * FROM inventory WHERE primary_name = ?";
	}

	public static String deleteResourceSetWithId() {
		return "DELETE FROM inventory WHERE id = ?";
	}
	
	public static String getLastInsertedId() {
		return "SELECT LAST_INSERT_ID()";
	}

	public static String updateResourceSetWithAmount() {
		return "UPDATE inventory SET amount = ? WHERE id = ?";
	}

	public static String insertWithNameAndAmount() {
		return "INSERT INTO inventory (primary_name, amount) VALUES (?, ?)";
	}


}
