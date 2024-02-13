package edu.kit.pms.im.message.handlers;

import java.io.Closeable;

public interface MessageHandler extends Closeable {
	
	public void handleMessages();
	
	public MessageHandler allocateRessources();

}
