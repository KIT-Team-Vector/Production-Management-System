package messageHandlers;

import java.io.Closeable;

public interface MessageHandler extends Closeable {
	
	public void handleMessages();
	
	public MessageHandler allocateRessources();

}
