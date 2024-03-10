package edu.kit.pms.im.common.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.kit.pms.im.common.services.MessageSenderService;
import edu.kit.pms.im.domain.InventoryManagementError;
import edu.kit.pms.im.domain.ResourceImpl;
import edu.kit.pms.im.domain.ResourceSet;
import edu.kit.pms.im.domain.ResourceSetImpl;
import edu.kit.pms.im.domain.ResourceSetRepository;
import edu.kit.pms.im.inventory.InventoryManager;
import edu.kit.pms.im.inventory.InventoryManagerImpl;

public class InventoryControllerTest {

	private static InventoryControllerImpl inventoryController;
	
	private static InventoryManager mockInventoryManager;
	
	private static MessageSenderService mockMessageSenderService;
	
	private ResourceSet testResourceSet;
	

	@Before
	public void setUp() {
		mockInventoryManager = mock(InventoryManager.class);
		mockMessageSenderService = mock(MessageSenderService.class);
		InventoryControllerImpl.removeInstance();
		InventoryControllerImpl.init(mockInventoryManager, mockMessageSenderService);
		inventoryController = InventoryControllerImpl.getInstance();
		testResourceSet = new ResourceSetImpl(new ResourceImpl(10011, "testResource"), 10);
	}
	
	@Test
	public void testInitControllerAsSingleton() {
		// Given,When,Then
		// No double initialization for singleton
		assertThrows(AssertionError.class, () -> {
			InventoryControllerImpl.init(mockInventoryManager, mockMessageSenderService);;
		});
	}
	
	@Test
	public void testRemoveInstance() {
		// Given
		assertTrue(InventoryControllerImpl.isInstanceCreated());
		
		// When
		InventoryControllerImpl.removeInstance();
		
		// Then
		assertFalse(InventoryControllerImpl.isInstanceCreated());
	}

	@Test
	public void testGetResourceSet() {
		// Given
		when(mockInventoryManager.getResourceSet(testResourceSet.resource().id())).thenReturn(testResourceSet);

		// When
		ResourceSet result = inventoryController.getResourceSet(testResourceSet.resource().id());

		// Then
		assertEquals(testResourceSet, result);
		verify(mockInventoryManager).getResourceSet(testResourceSet.resource().id());
	}
	
	@Test
	public void testAddResourceSet() {
		// Given
		when(mockInventoryManager.addResourceSet(testResourceSet)).thenReturn(testResourceSet);
		
		// When
		ResourceSet result = inventoryController.addResourceSet(testResourceSet);

		// Then
		assertEquals(testResourceSet, result);
		verify(mockInventoryManager).addResourceSet(testResourceSet);
	}

	@Test
	public void testAddResourceSetWithError() {
		// Given
		InventoryManagementError error = new InventoryManagementError("TestTyp", "testMessage", 11122);
		when(mockInventoryManager.addResourceSet(testResourceSet)).thenThrow(error);
		
		// When
		ResourceSet result = inventoryController.addResourceSet(testResourceSet);

		// Then
		assertNull(result);
		verify(mockInventoryManager).addResourceSet(testResourceSet);
		verify(mockMessageSenderService).sendError(null, error);
	}

	@Test
	public void testRemoveResourceSet() {
		// Given
		int resourceId = 123;
		when(mockInventoryManager.deleteResourceSet(resourceId)).thenReturn(true);

		// When
		boolean result = inventoryController.removeResourceSet(resourceId);

		// Then
		assertTrue(result);
		verify(mockInventoryManager).deleteResourceSet(resourceId);
	}

	@Test
	public void testDecreaseResourceSet() {
		// Given
		Long key = 456L;
		when(mockInventoryManager.decreaseResourceSet(testResourceSet)).thenReturn(true, false);

		// When
		inventoryController.decreaseResourceSet(key, testResourceSet);
		inventoryController.decreaseResourceSet(key, testResourceSet);

		// Then
		verify(mockInventoryManager, times(2)).decreaseResourceSet(testResourceSet);
		verify(mockMessageSenderService).sendDecreaseResourceSetResponse(key, true);
		verify(mockMessageSenderService).sendDecreaseResourceSetResponse(key, false);
	}
	
	@Test
	public void testDecreaseResourceSetWithError() {
		// Given
		Long key = 456L;
		InventoryManagementError error = new InventoryManagementError("TestTyp", "error while decreaseResourceSet", 11122);
		when(mockInventoryManager.decreaseResourceSet(testResourceSet)).thenThrow(error);

		// When
		inventoryController.decreaseResourceSet(key, testResourceSet);

		// Then
		verify(mockInventoryManager).decreaseResourceSet(testResourceSet);
		verify(mockMessageSenderService).sendError(key, error);
		verify(mockMessageSenderService).sendDecreaseResourceSetResponse(key, false);
	}
}
