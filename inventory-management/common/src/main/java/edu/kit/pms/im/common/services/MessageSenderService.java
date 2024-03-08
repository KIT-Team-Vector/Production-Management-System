package edu.kit.pms.im.common.services;

import edu.kit.pms.im.domain.InventoryManagementError;

public interface MessageSenderService {
	
	public void sendError(Long key, InventoryManagementError mError);
	
	public void sendDecreaseResourceSetResponse(Long key, Boolean success);
	
	public void stop();

}
