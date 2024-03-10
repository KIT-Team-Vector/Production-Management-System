package edu.kit.pms.im.common.services;

public interface MessageReceiverService extends Runnable {
	
	public boolean isRunning();
	
	public void stop();
	
	public void run();

}
