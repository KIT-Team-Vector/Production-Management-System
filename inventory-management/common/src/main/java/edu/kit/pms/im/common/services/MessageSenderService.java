package edu.kit.pms.im.common.services;

import edu.kit.pms.im.domain.InventoryManagementError;

/**
 * Service which sends messages to a message based architecture
 */
public interface MessageSenderService {
	
	/**
	 * Sends error message
	 * 
	 * @param key of the message
	 * @param mError to send
	 */
	public void sendError(Long key, InventoryManagementError mError);
	
	/**
	 * Sends a response from a DecreaseResourceSet request
	 * 
	 * @param key of the original request
	 * @param success if the operation was successfully
	 */
	public void sendDecreaseResourceSetResponse(Long key, Boolean success);
	
	/**
	 * stops the service and deallocates resources
	 */
	public void stop();

}
