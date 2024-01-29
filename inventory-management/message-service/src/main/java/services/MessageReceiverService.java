package services;

public interface MessageReceiverService extends Runnable {
	
	public boolean isRunning();
	
	public void stop();

}
