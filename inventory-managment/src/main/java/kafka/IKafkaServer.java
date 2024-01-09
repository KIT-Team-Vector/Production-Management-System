package kafka;

import domain.RessourceSet;

public interface IKafkaServer {
	
	public boolean isRunning();
	
	public void start();
	
	public void stop();
	
	public void sendRessourceSet(RessourceSet ressourceSet);
	
	
}
