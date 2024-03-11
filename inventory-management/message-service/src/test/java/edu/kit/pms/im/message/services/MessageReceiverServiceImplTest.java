package edu.kit.pms.im.message.services;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.pms.im.message.handlers.MessageHandler;

public class MessageReceiverServiceImplTest {
	
	private MessageReceiverServiceImpl messageReceiverService;
    private MessageHandler mockMessageHandler;
    private Thread messageReceiverThread;

    @Before
    public void setUp() {
        mockMessageHandler = mock(MessageHandler.class);
        messageReceiverService = new MessageReceiverServiceImpl(mockMessageHandler);
		messageReceiverThread = new Thread(messageReceiverService);
    }
    
    
    @After
    public void stoppService() {
    	messageReceiverService.stop();
    	try {
			messageReceiverThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

    @Test
    public void testRunningAndStopping() {
    	// Given
    	when(mockMessageHandler.allocateResources()).thenReturn(mockMessageHandler);
    	// When
    	messageReceiverThread.start();
    	try {
			Thread.sleep(100);
			messageReceiverService.stop();
	    	messageReceiverThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
        // Then
        verify(mockMessageHandler).allocateResources();
        verify(mockMessageHandler, atLeastOnce()).handleMessages();
    }
    
    @Test
    public void testIsRunning() {
    	// Given
    	when(mockMessageHandler.allocateResources()).thenReturn(mockMessageHandler);
    	messageReceiverThread.start();
    	try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    	// When
    	boolean result = messageReceiverService.isRunning();
    	
        // Then
        assertTrue(result);
    }
}
