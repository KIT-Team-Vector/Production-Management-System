package services;

import java.io.IOException;

import messageHandlers.MessageHandler;

public class MessageReceiverServiceImpl implements MessageReceiverService {

	private boolean isRunning;
	private MessageHandler messageHandler;

	public MessageReceiverServiceImpl(MessageHandler messageHandler) {
		isRunning = false;
		this.messageHandler = messageHandler;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void stop() {
		isRunning = false;
	}

	public void run() {
		isRunning = true;
		executeMessageHandler();	
	}
	
	private void executeMessageHandler() {
		try (MessageHandler messageHandler = this.messageHandler.allocateRessources()){
			while (isRunning) {
				messageHandler.handleMessages();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
}
