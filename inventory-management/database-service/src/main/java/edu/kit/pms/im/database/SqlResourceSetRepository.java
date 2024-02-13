package edu.kit.pms.im.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import edu.kit.pms.im.domain.ResourceSetRepository;
import edu.kit.pms.im.domain.Resource;
import edu.kit.pms.im.domain.ResourceSet;

public class SqlResourceSetRepository implements ResourceSetRepository {

	public Collection<ResourceSet> getAll() {
		Collection<ResourceSet> ressourceSets = new ArrayList<>();
		try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventory",
				"inventory_managment_admin", "Inv1123581321");
				PreparedStatement stmt = con.prepareStatement(SqlStatementGenerator.selectAllResourceSets())) {
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				Resource ressource = new Resource(resultSet.getInt("id"), resultSet.getString("primary_name"));
				ResourceSet ressourceSet = new ResourceSet(ressource, resultSet.getInt("amount"));
				ressourceSets.add(ressourceSet);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ressourceSets;
	}

	@Override
	public ResourceSet get(int id) {
		ResourceSet ressourceSet;
		try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventory",
				"inventory_managment_admin", "Inv1123581321");
				PreparedStatement stmt = con.prepareStatement(SqlStatementGenerator.selectResourceSetWithId())) {

			stmt.setInt(1, id);
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				ressourceSet = getResourceSet(resultSet);
				return ressourceSet;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void add(ResourceSet ressourceSet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAmount(int id, int deltaAount) {
		try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventory",
				"inventory_managment_admin", "Inv1123581321");
				PreparedStatement selectStatement = con
						.prepareStatement(SqlStatementGenerator.selectAndLockAmountWithId());
				PreparedStatement updateStatement = con
						.prepareStatement(SqlStatementGenerator.updateResourceSetWithAmount())) {
			con.setAutoCommit(false);
			selectStatement.setInt(1, id);
			ResultSet resultSet = selectStatement.executeQuery();
			if (resultSet.next()) {
				int oldAmount = resultSet.getInt("amount");
				int newAmount = oldAmount - deltaAount;

				if (newAmount < 0) {
					throw new IllegalArgumentException();
				}
				updateStatement.setInt(1, newAmount);
				updateStatement.setInt(2, id);
				updateStatement.executeUpdate();
			}

			con.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private ResourceSet getResourceSet(ResultSet resultSet) throws SQLException {
		return new ResourceSet(getResource(resultSet), getAmount(resultSet));
	}

	private Resource getResource(ResultSet resultSet) throws SQLException {
		return new Resource(getId(resultSet), getName(resultSet));
	}
	
	private int getId(ResultSet resultSet) throws SQLException {
		return resultSet.getInt("id");
	}
	
	private String getName(ResultSet resultSet) throws SQLException {
		return resultSet.getString("primary_name");
	}
	
	private int getAmount(ResultSet resultSet) throws SQLException {
		return resultSet.getInt("amount");
	}

}
