package edu.kit.pms.im.message.handlers;

import java.io.Closeable;

/**
 * MessageHandler for message based polling
 */
public interface MessageHandler extends Closeable {
	
	/**
	 * handles the polling and forwards the request to the controller
	 */
	public void handleMessages();
	
	/**
	 * allocates the necessary resources for polling
	 * @return MessageHandler with polling resources
	 */
	public MessageHandler allocateResources();

}
