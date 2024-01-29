package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import domain.Inventory;
import domain.Resource;
import domain.ResourceSet;

public class DataBaseConnector implements Inventory {
	
	private SqlStatementGenerator sqlStatementGenerator;
	
	public DataBaseConnector() {
		sqlStatementGenerator = new SqlStatementGenerator();
	}

	public Collection<ResourceSet> getAllRessourceSets() {
		Collection<ResourceSet> ressourceSets = new ArrayList<>();
		try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventory",
				"inventory_managment_admin", "Inv1123581321");
				Statement stmt = con.createStatement();
				ResultSet resultSet = stmt.executeQuery(sqlStatementGenerator.getSelectAllItems())) {
			while (resultSet.next()) {
				Resource ressource = new Resource(resultSet.getInt("id"), resultSet.getString("primary_name"));
				ResourceSet ressourceSet = new ResourceSet(ressource, resultSet.getInt("amount"));
				ressourceSets.add(ressourceSet);
				System.out.println(ressource.getId() + " " + ressource.getName() + " " + ressourceSet.getAmount());
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ressourceSets;
	}

	@Override
	public ResourceSet getRessourceSetById(String id) {
		ResourceSet ressourceSet;
		try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventory",
				"inventory_managment_admin", "Inv1123581321");
				Statement stmt = con.createStatement();
				ResultSet resultSet = stmt.executeQuery(sqlStatementGenerator.getSelectItemWithId(id))) {
			while (resultSet.next()) {
				Resource ressource = new Resource(resultSet.getInt("id"), resultSet.getString("primary_name"));
				ressourceSet = new ResourceSet(ressource, resultSet.getInt("amount"));
				System.out.println(ressource.getId() + " " + ressource.getName() + " " + ressourceSet.getAmount());
				return ressourceSet;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Collection<ResourceSet> getRessourceSetByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addRessourceSet(ResourceSet item) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addRessourceSets(Collection<ResourceSet> items) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteRessourceSet(String id) {
		// TODO Auto-generated method stub

	}
}
