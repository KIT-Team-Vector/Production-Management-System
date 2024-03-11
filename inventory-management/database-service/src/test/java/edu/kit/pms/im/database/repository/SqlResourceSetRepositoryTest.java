package edu.kit.pms.im.database.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import edu.kit.pms.im.database.connection.ConnectionProvider;
import edu.kit.pms.im.domain.InventoryManagementError;
import edu.kit.pms.im.domain.ResourceImpl;
import edu.kit.pms.im.domain.ResourceSet;
import edu.kit.pms.im.domain.ResourceSetImpl;

public class SqlResourceSetRepositoryTest {
	private ConnectionProvider connectionProvider;
	private Connection connection;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;
	private ResourceSetImpl testResourceSet;
	SqlResourceSetRepository repository;

	@Before
	public void setup() throws SQLException {
		// Mocking connection provider and database objects
		connectionProvider = mock(ConnectionProvider.class);
		connection = mock(Connection.class);
		preparedStatement = mock(PreparedStatement.class);
		resultSet = mock(ResultSet.class);
		repository = new SqlResourceSetRepository(connectionProvider);

		// Setting up mock behavior
		when(connectionProvider.getConnection()).thenReturn(connection);
		when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
		when(connection.prepareStatement(any(String.class), any(Integer.class))).thenReturn(preparedStatement);
		when(preparedStatement.executeQuery()).thenReturn(resultSet);

		testResourceSet = new ResourceSetImpl(new ResourceImpl(10011, "testResource"), 10);
	}

	@Test
	public void testGetAll() throws SQLException {
		// Given
		ArrayList<ResourceSet> expectedResourceSets = new ArrayList<>();
		expectedResourceSets.add(testResourceSet);

		when(resultSet.next()).thenReturn(true).thenReturn(false);
		when(resultSet.getInt("id")).thenReturn(testResourceSet.resource().id());
		when(resultSet.getString("primary_name")).thenReturn(testResourceSet.resource().name());																						
		when(resultSet.getInt("amount")).thenReturn(testResourceSet.amount()); 

		// When
		ArrayList<ResourceSet> actualResourceSets = new ArrayList<>(repository.getAll());

		// Then
		assertNotNull(actualResourceSets);
		assertEquals(expectedResourceSets.size(), actualResourceSets.size());
		assertEquals(expectedResourceSets.get(0), actualResourceSets.get(0));
	}

	@Test
	public void testGet() throws SQLException {
		// Given
		int resourceId = testResourceSet.resource().id();
		when(resultSet.next()).thenReturn(true);
		when(resultSet.getInt("id")).thenReturn(resourceId);
		when(resultSet.getString("primary_name")).thenReturn(testResourceSet.resource().name());
		when(resultSet.getInt("amount")).thenReturn(testResourceSet.amount());

		// When
		ResourceSet result = repository.get(resourceId);

		// Then
		assertNotNull(result);
		assertEquals(testResourceSet, result);
	}

	@Test
	public void testGetWithNoMathingResourceSet() throws SQLException {
		// Given
		int resourceId = testResourceSet.resource().id();
		when(resultSet.next()).thenReturn(false);

		// When
		ResourceSet result = repository.get(resourceId);

		// Then
		assertNull(result);
	}

	@Test
	public void testAdd() throws SQLException {
		// Given
		when(preparedStatement.executeUpdate()).thenReturn(1);
		when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(true);
		when(resultSet.getInt(1)).thenReturn(10012);

		// When
		ResourceSet result = repository.add(testResourceSet.resource().name(), testResourceSet.amount());

		// Then
		verify(preparedStatement).setString(1, testResourceSet.resource().name());
		verify(preparedStatement).setInt(2, testResourceSet.amount());
		assertNotNull(result);
		assertEquals(10012, result.resource().id());
		assertEquals(testResourceSet.resource().name(), result.resource().name());
		assertEquals(testResourceSet.amount(), result.amount());
	}

	@Test
	public void testDelete() throws SQLException {
		// Given
		int resourceIdToDelete = 10011;
		when(preparedStatement.executeUpdate()).thenReturn(1);

		// When
		boolean result = repository.delete(resourceIdToDelete);

		// Then
		assertTrue(result);
	}

	@Test
	public void testUpdateAmount() throws SQLException, InventoryManagementError {
		// Given
		int resourceId = testResourceSet.resource().id();
		int deltaAmount = 5;
		when(resultSet.next()).thenReturn(true);
		when(resultSet.getInt("amount")).thenReturn(testResourceSet.amount());
		when(preparedStatement.executeUpdate()).thenReturn(1);

		// When
		boolean result = repository.updateAmount(resourceId, deltaAmount);

		// Then
		assertTrue(result);
	}

	@Test
	public void testUpdateAmountWithNegativeTotalAmount() throws SQLException, InventoryManagementError {
		// Given
		int resourceId = testResourceSet.resource().id();
		// remove more than exists
		int deltaAmount = -(testResourceSet.amount() + 1);
		when(resultSet.next()).thenReturn(true);
		when(resultSet.getInt("amount")).thenReturn(testResourceSet.amount());
		when(preparedStatement.executeUpdate()).thenReturn(1);

		// When Then
		assertThrows(InventoryManagementError.class, () -> {
			boolean result = repository.updateAmount(resourceId, deltaAmount);
			assertFalse(result);
		});
		// mo update executed to prevent negative amount values
		verify(preparedStatement, times(0)).executeUpdate();
	}

	@Test
	public void testUpdateAmountWithZeroAmount() throws SQLException, InventoryManagementError {
		// Given
		int resourceId = testResourceSet.resource().id();
		// remove more than exists
		int deltaAmount = -(testResourceSet.amount());
		when(resultSet.next()).thenReturn(true);
		when(resultSet.getInt("amount")).thenReturn(testResourceSet.amount());
		when(preparedStatement.executeUpdate()).thenReturn(1);

		// When Then
		boolean result = repository.updateAmount(resourceId, deltaAmount);

		// Then
		assertTrue(result);
		verify(preparedStatement, times(1)).executeUpdate();
	}
}
