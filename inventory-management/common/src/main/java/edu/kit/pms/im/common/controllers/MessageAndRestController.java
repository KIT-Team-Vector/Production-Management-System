package edu.kit.pms.im.common.controllers;

import edu.kit.pms.im.common.concepts.ResourceSetImpl;

import edu.kit.pms.im.common.services.MessageSenderService;
import edu.kit.pms.im.domain.MicroserviceError;
import edu.kit.pms.im.domain.ResourceSet;
import edu.kit.pms.im.inventory.InventoryManager;

public class MessageAndRestController implements ExternalInterfaceController {

	private static MessageAndRestController INSTANCE;

	private InventoryManager inventoryManager;

	private MessageSenderService messageSenderService;

	private MessageAndRestController(InventoryManager inventoryManager, MessageSenderService messageSenderService) {
		super();
		this.inventoryManager = inventoryManager;
		this.messageSenderService = messageSenderService;
	}

	public synchronized static void init(InventoryManager inventoryManager,
			MessageSenderService messageSenderService) {
		if (INSTANCE != null) {
			throw new AssertionError("You already initialized me");
		}
		INSTANCE = new MessageAndRestController(inventoryManager, messageSenderService);
	}
	
	public static MessageAndRestController getInstance() {
        if(INSTANCE == null) {
            throw new AssertionError("You have to call init first");
        }
        return INSTANCE;
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
		return inventoryManager.addResourceSet(resourceSet);
	}

	@Override
	public boolean removeResourceSet(int id) {
		return inventoryManager.removeResourceSet(id);
	}

	@Override
	public void changeAmountOfResource(Long key, ResourceSet resourceSet) {
		boolean success = false;
		try {
			success = inventoryManager.changeAmountOfResource(resourceSet);
		} catch (MicroserviceError e) {
			e.printStackTrace();
			messageSenderService.sendError(key, e);
		} finally {
			messageSenderService.sendChangeAmountOfResourceResponse(key, Boolean.valueOf(success));
		}
	}

}
