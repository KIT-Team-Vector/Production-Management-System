package services;

import domain.MicroserviceError;

public interface MessageSenderService {
	
	public void sendError(MicroserviceError mError);
	
	public void sendDeleteFromInventoryResponse(Boolean success);
	
	public void stop();

}
