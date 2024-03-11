package edu.kit.pms.im.common.controllers;
import edu.kit.pms.im.common.services.MessageSenderService;

import edu.kit.pms.im.domain.InventoryManagementError;
import edu.kit.pms.im.domain.ResourceSet;
import edu.kit.pms.im.inventory.InventoryManager;

public class InventoryControllerImpl implements InventoryController {

	private static InventoryControllerImpl INSTANCE;

	private InventoryManager inventoryManager;

	private MessageSenderService messageSenderService;

	private InventoryControllerImpl(InventoryManager inventoryManager, MessageSenderService messageSenderService) {
		super();
		this.inventoryManager = inventoryManager;
		this.messageSenderService = messageSenderService;
	}

	public synchronized static void init(InventoryManager inventoryManager,
			MessageSenderService messageSenderService) {
		if (INSTANCE != null) {
			throw new AssertionError("You already initialized me");
		}
		INSTANCE = new InventoryControllerImpl(inventoryManager, messageSenderService);
	}
	
	public static InventoryControllerImpl getInstance() {
        if(INSTANCE == null) {
            throw new AssertionError("You have to call init first");
        }
        return INSTANCE;
    }
	
	public static void removeInstance() {
		INSTANCE = null;
	}

	public static boolean isInstanceCreated() {
		return INSTANCE != null;
	}

	@Override
	public ResourceSet getResourceSet(int id) {
		return inventoryManager.getResourceSet(id);
	}

	@Override
	public ResourceSet addResourceSet(ResourceSet resourceSet) {
		try {
		return inventoryManager.addResourceSet(resourceSet);
		} catch (InventoryManagementError e) {
			e.printStackTrace();
			messageSenderService.sendError(null, e);
			return null;
		} 
	}

	@Override
	public boolean removeResourceSet(int id) {
		return inventoryManager.deleteResourceSet(id);
	}

	@Override
	public void decreaseResourceSet(Long key, ResourceSet resourceSet) {
		boolean success = false;
		try {
			success = inventoryManager.decreaseResourceSet(resourceSet);
		} catch (InventoryManagementError e) {
			e.printStackTrace();
			messageSenderService.sendError(key, e);
		} finally {
			messageSenderService.sendDecreaseResourceSetResponse(key, Boolean.valueOf(success));
		}
	}

}
