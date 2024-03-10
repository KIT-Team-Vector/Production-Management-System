package edu.kit.pms.im.database.repository;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import edu.kit.pms.im.database.connection.ConnectionProvider;
import edu.kit.pms.im.domain.InventoryManagementError;
import edu.kit.pms.im.domain.ResourceImpl;
import edu.kit.pms.im.domain.ResourceSet;
import edu.kit.pms.im.domain.ResourceSetImpl;
import edu.kit.pms.im.domain.ResourceSetRepository;

public class SqlResourceSetRepository implements ResourceSetRepository {
	
	private ConnectionProvider connectionProvider;
	
	public SqlResourceSetRepository(ConnectionProvider connectionProvider) {
		this.connectionProvider = connectionProvider;   
	}

	public Collection<ResourceSet> getAll() {
		Collection<ResourceSet> resourceSets = new ArrayList<>();
		try (Connection con = connectionProvider.getConnection();
				PreparedStatement stmt = con.prepareStatement(SqlStatementGenerator.selectAllResourceSets())) {
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				ResourceSet resourceSet = getResourceSet(resultSet);
				resourceSets.add(resourceSet);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resourceSets;
	}

	@Override
	public ResourceSet get(int id) {
		ResourceSet resourceSet;
		try (Connection con = connectionProvider.getConnection();
				PreparedStatement stmt = con.prepareStatement(SqlStatementGenerator.selectResourceSetWithId())) {

			stmt.setInt(1, id);
			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				resourceSet = getResourceSet(resultSet);
				return resourceSet;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ResourceSet add(String name, int amount) {
		try (Connection con = connectionProvider.getConnection();
				PreparedStatement addStatement = con.prepareStatement(SqlStatementGenerator.insertWithNameAndAmount(), PreparedStatement.RETURN_GENERATED_KEYS)) {
			
			addStatement.setString(1, name);
			addStatement.setInt(2, amount);
			addStatement.executeUpdate();
			
			ResultSet resultSet = addStatement.getGeneratedKeys();
			if (resultSet.next()) {
		        int lastInsertedId = resultSet.getInt(1);
		        if (lastInsertedId > 0) {
		        	return new ResourceSetImpl(new ResourceImpl(lastInsertedId, name), amount);
		        }
		    }
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	@Override
	public boolean delete(int id) {
		int rowInserted = 0;
		try (Connection con = connectionProvider.getConnection();
				PreparedStatement stmt = con.prepareStatement(SqlStatementGenerator.deleteResourceSetWithId())) {

			stmt.setInt(1, id);
			rowInserted = stmt.executeUpdate();
			return rowInserted > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateAmount(int id, int deltaAmount) throws InventoryManagementError {
		try (Connection con = connectionProvider.getConnection();
				PreparedStatement selectStatement = con
						.prepareStatement(SqlStatementGenerator.selectAndLockResourceSetWithId());
				PreparedStatement updateStatement = con
						.prepareStatement(SqlStatementGenerator.updateResourceSetWithAmount())) {
			con.setAutoCommit(false);
			selectStatement.setInt(1, id);
			ResultSet resultSet = selectStatement.executeQuery();

			if (resultSet.next()) {
				int oldAmount = resultSet.getInt("amount");
				int newAmount = oldAmount + deltaAmount;

				if (newAmount < 0) {
					throw new InventoryManagementError("Incorrect_Amount", "The amount of the resource with id " + id
							+ " cannot fall below zero, available amount: " + oldAmount);
				}
				updateStatement.setInt(1, newAmount);
				updateStatement.setInt(2, id);
				updateStatement.executeUpdate();

			} else {
				throw new InventoryManagementError("No_Resource", "The resource with " + id + " does not exist");
			}

			con.commit();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

	private ResourceSet getResourceSet(ResultSet resultSet) throws SQLException {
		return new ResourceSetImpl(getResource(resultSet), getAmount(resultSet));
	}

	private ResourceImpl getResource(ResultSet resultSet) throws SQLException {
		return new ResourceImpl(getId(resultSet), getName(resultSet));
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
