package edu.kit.pms.im.inventory;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import edu.kit.pms.im.domain.InventoryManagementError;
import edu.kit.pms.im.domain.ResourceImpl;
import edu.kit.pms.im.domain.ResourceSet;
import edu.kit.pms.im.domain.ResourceSetImpl;
import edu.kit.pms.im.domain.ResourceSetRepository;

public class InventoryManagerTest {

	private InventoryManagerImpl inventoryManager;
	
	private ResourceSetRepository mockRepository;
	
	private ResourceSet testResourceSetWithPositiveAmount;
	private ResourceSet testResourceSetWithNegativeAmount;

	@Before
	public void setUp() {
		mockRepository = mock(ResourceSetRepository.class);
		inventoryManager = new InventoryManagerImpl(mockRepository);
		testResourceSetWithPositiveAmount = new ResourceSetImpl(new ResourceImpl(10011, "testResource"), 10);
		testResourceSetWithNegativeAmount = new ResourceSetImpl(new ResourceImpl(10010, "testResourceNegative"), -10);
	}

	@Test
	public void testGetResourceSetWithExistingId() {
		// Given:
		when(mockRepository.get(testResourceSetWithPositiveAmount.resource().id()))
				.thenReturn(testResourceSetWithPositiveAmount);

		// When:
		ResourceSet result = inventoryManager.getResourceSet(testResourceSetWithPositiveAmount.resource().id());

		// Then:
		verify(mockRepository).get(testResourceSetWithPositiveAmount.resource().id());
		assertSame(testResourceSetWithPositiveAmount, result);
	}

	@Test
	public void testGetResourceSetWithNotExistingId() {
		// Given:
		when(mockRepository.get(testResourceSetWithPositiveAmount.resource().id())).thenReturn(null);

		// When:
		ResourceSet result = inventoryManager.getResourceSet(testResourceSetWithPositiveAmount.resource().id());

		// Then:
		verify(mockRepository).get(testResourceSetWithPositiveAmount.resource().id());
		assertNull(result);
	}

	@Test
	public void testAddResourceSetWithExistingMatchingResourceSet() {
		// Given
		ResourceSet existingTestResourceSet = new ResourceSetImpl(
				(ResourceImpl) testResourceSetWithPositiveAmount.resource(), 10);
		ResourceSet resultResourceSetWithExpectedFinalAmount = new ResourceSetImpl(
				(ResourceImpl) testResourceSetWithPositiveAmount.resource(),
				testResourceSetWithPositiveAmount.amount() + existingTestResourceSet.amount());
		when(mockRepository.get(testResourceSetWithPositiveAmount.resource().id()))
				.thenReturn(testResourceSetWithPositiveAmount, resultResourceSetWithExpectedFinalAmount);
		when(mockRepository.updateAmount(anyInt(), anyInt())).thenReturn(true);
		when(mockRepository.add(anyString(), anyInt())).thenReturn(testResourceSetWithPositiveAmount);

		// When
		ResourceSet result = inventoryManager.addResourceSet(testResourceSetWithPositiveAmount);

		// Then
		verify(mockRepository, times(2)).get(testResourceSetWithPositiveAmount.resource().id());
		verify(mockRepository, times(1)).updateAmount(testResourceSetWithPositiveAmount.resource().id(),
				Math.abs(testResourceSetWithPositiveAmount.amount()));
		verify(mockRepository, times(0)).add(testResourceSetWithPositiveAmount.resource().name(),
				testResourceSetWithPositiveAmount.amount());
		assertSame(resultResourceSetWithExpectedFinalAmount, result);
	}

	@Test
	public void testAddResourceSetWithoutMatchingResourceSet() {
		// Given
		ResourceSet resultResourceSetWithExpectedFinalAmount = new ResourceSetImpl(
				new ResourceImpl(1111, testResourceSetWithPositiveAmount.resource().name()),
				testResourceSetWithPositiveAmount.amount());
		when(mockRepository.get(testResourceSetWithPositiveAmount.resource().id())).thenReturn(null);
		when(mockRepository.add(anyString(), anyInt())).thenReturn(resultResourceSetWithExpectedFinalAmount);

		// When
		ResourceSet result = inventoryManager.addResourceSet(testResourceSetWithPositiveAmount);

		// Then
		verify(mockRepository, times(1)).get(testResourceSetWithPositiveAmount.resource().id());
		verify(mockRepository, times(1)).add(testResourceSetWithPositiveAmount.resource().name(),
				testResourceSetWithPositiveAmount.amount());
		assertSame(resultResourceSetWithExpectedFinalAmount, result);
	}

	@Test
	public void testAddResourceSetWithoutMatchingResourceSetName() {
		// Given
		ResourceSet existingTestResourceSet = new ResourceSetImpl(
				new ResourceImpl(testResourceSetWithPositiveAmount.resource().id(), "diffrentName"), 10);
		ResourceSet resultResourceSetWithExpectedFinalAmount = new ResourceSetImpl(
				new ResourceImpl(1111, testResourceSetWithPositiveAmount.resource().name()),
				testResourceSetWithPositiveAmount.amount());
		when(mockRepository.get(testResourceSetWithPositiveAmount.resource().id())).thenReturn(existingTestResourceSet);
		when(mockRepository.add(anyString(), anyInt())).thenReturn(resultResourceSetWithExpectedFinalAmount);

		// When
		ResourceSet result = inventoryManager.addResourceSet(testResourceSetWithPositiveAmount);

		// Then
		verify(mockRepository, times(1)).get(testResourceSetWithPositiveAmount.resource().id());
		verify(mockRepository, times(1)).add(testResourceSetWithPositiveAmount.resource().name(),
				testResourceSetWithPositiveAmount.amount());
		assertSame(resultResourceSetWithExpectedFinalAmount, result);
	}

	@Test
	public void testDeleteResourceSet() {
		// Given
		int resourceIdToDelete = 123;
		when(mockRepository.delete(resourceIdToDelete)).thenReturn(true, false);

		// When
		boolean result1 = inventoryManager.deleteResourceSet(resourceIdToDelete);
		boolean result2 = inventoryManager.deleteResourceSet(resourceIdToDelete);

		// Then
		assertTrue(result1);
		assertFalse(result2);
	}

	@Test
	public void testIncreaseResourceSet() {
		// Given
		when(mockRepository.updateAmount(testResourceSetWithPositiveAmount.resource().id(),
				testResourceSetWithPositiveAmount.amount())).thenReturn(true, false);
		when(mockRepository.updateAmount(testResourceSetWithNegativeAmount.resource().id(),
				Math.abs(testResourceSetWithNegativeAmount.amount()))).thenReturn(true);

		// When
		boolean result1 = inventoryManager.increaseResourceSet(testResourceSetWithPositiveAmount);
		boolean result2 = inventoryManager.increaseResourceSet(testResourceSetWithPositiveAmount);
		boolean result3 = inventoryManager.increaseResourceSet(testResourceSetWithNegativeAmount);

		// Then
		verify(mockRepository, times(2)).updateAmount(testResourceSetWithPositiveAmount.resource().id(),
				testResourceSetWithPositiveAmount.amount());
		verify(mockRepository, times(1)).updateAmount(testResourceSetWithNegativeAmount.resource().id(),
				Math.abs(testResourceSetWithNegativeAmount.amount()));
		assertTrue(result1);
		assertFalse(result2);
		assertTrue(result3);
	}

	@Test
	public void testDecreaseResourceSet() {
		// Given
		when(mockRepository.updateAmount(testResourceSetWithNegativeAmount.resource().id(),
				testResourceSetWithNegativeAmount.amount())).thenReturn(true, false);
		when(mockRepository.updateAmount(testResourceSetWithPositiveAmount.resource().id(),
				-Math.abs(testResourceSetWithPositiveAmount.amount()))).thenReturn(true);

		// When
		boolean result1 = inventoryManager.decreaseResourceSet(testResourceSetWithNegativeAmount);
		boolean result2 = inventoryManager.decreaseResourceSet(testResourceSetWithNegativeAmount);
		boolean result3 = inventoryManager.decreaseResourceSet(testResourceSetWithPositiveAmount);

		// Then
		verify(mockRepository, times(2)).updateAmount(testResourceSetWithNegativeAmount.resource().id(),
				testResourceSetWithNegativeAmount.amount());
		verify(mockRepository, times(1)).updateAmount(testResourceSetWithPositiveAmount.resource().id(),
				-Math.abs(testResourceSetWithPositiveAmount.amount()));
		assertTrue(result1);
		assertFalse(result2);
		assertTrue(result3);
	}

	@Test
	public void testErrorThrowing() {
		// Given
		when(mockRepository.updateAmount(testResourceSetWithPositiveAmount.resource().id(),
				testResourceSetWithPositiveAmount.amount())).thenThrow(new InventoryManagementError("TestTyp", "testMessage", 11122));
		
		// When, Then
		assertThrows(InventoryManagementError.class, () -> {
			inventoryManager.increaseResourceSet(testResourceSetWithPositiveAmount);;
	    });
		verify(mockRepository, times(1)).updateAmount(testResourceSetWithPositiveAmount.resource().id(),
				testResourceSetWithPositiveAmount.amount());
	}
}
