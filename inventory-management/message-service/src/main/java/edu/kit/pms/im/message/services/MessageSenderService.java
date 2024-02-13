package edu.kit.pms.im.message.services;

import edu.kit.pms.im.domain.MicroserviceError;

public interface MessageSenderService {
	
	public void sendError(MicroserviceError mError);
	
	public void sendDeleteFromInventoryResponse(Boolean success);
	
	public void stop();

}
