package edu.kit.pms.im.common.services;

import edu.kit.pms.im.domain.MicroserviceError;

public interface MessageSenderService {
	
	public void sendError(Long key, MicroserviceError mError);
	
	public void sendDecreaseResourceSetResponse(Long key, Boolean success);
	
	public void stop();

}
