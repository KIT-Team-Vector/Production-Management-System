package edu.kit.pms.im.common.services;

/**
 * Service which receives messages from a message based architecture
 */
public interface MessageReceiverService extends Runnable {
	
	/**
	 * if service is running
	 * 
	 * @return true if running
	 */
	public boolean isRunning();
	
	/**
	 * stops the service
	 */
	public void stop();
	
	/**
	 * runs the service
	 */
	public void run();

}
