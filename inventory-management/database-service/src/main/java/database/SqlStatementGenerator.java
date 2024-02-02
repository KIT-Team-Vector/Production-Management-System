package database;

public class SqlStatementGenerator {
	
	public String getSelectAllItems() {
		return "SELECT * FROM item";
	}
	
	public String getSelectItemWithId(String id) {
		return "SELECT * FROM item WHERE id = " + id;
	}
	
	public String getSelectItemsWithName(String name) {
		return "SELECT * FROM item WHERE primary_name = " + name;
	}

}
