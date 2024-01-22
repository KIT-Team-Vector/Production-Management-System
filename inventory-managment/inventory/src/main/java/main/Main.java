package main;


import org.apache.kafka.common.serialization.BooleanSerializer;

import database.DataBaseConnector;
import domain.MicroserviceError;
import domain.ResourceSet;
import kafka.clients.ConsumerFactory;
import kafka.clients.ProducerFactory;
import kafka.clients.SimpleConsumerFactory;
import kafka.clients.SimpleProducerFactory;
import serialization.MicroserviceErrorSerializer;
import serialization.RessourceSetDeserializer;
import services.MessageReceiverServiceImpl;
import services.MessageReceiverService;
import services.MessageSenderService;
import services.MessageSenderServiceImpl;
import messageHandlers.MessageHandler;
import messageHandlers.RemoveMessageHandler;

public class Main {

	public static void main(String[] args) {
		// database example calls
		DataBaseConnector databaseConnector = new DataBaseConnector();
		databaseConnector.getAllRessourceSets();
		databaseConnector.getRessourceSetById("1");
		
		
		// listen and handle kafka requests
		ConsumerFactory<ResourceSet, RessourceSetDeserializer> consumerFactory = new SimpleConsumerFactory<ResourceSet, RessourceSetDeserializer>(RessourceSetDeserializer.class);
		MessageHandler removeMessageHandler = new RemoveMessageHandler(consumerFactory);
		MessageReceiverService ks = new MessageReceiverServiceImpl(removeMessageHandler);
		Thread thread = new Thread(ks);
		thread.start();
		
		
		// send kafka Request
		ProducerFactory<MicroserviceError, MicroserviceErrorSerializer> errorProducer = new SimpleProducerFactory<MicroserviceError, MicroserviceErrorSerializer>(MicroserviceErrorSerializer.class);
		ProducerFactory<Boolean, BooleanSerializer> booleanProducer = new SimpleProducerFactory<Boolean, BooleanSerializer>(BooleanSerializer.class);
		MessageSenderService ms = new MessageSenderServiceImpl(errorProducer, booleanProducer);
		ms.sendDeleteFromInventoryResponse(false);
		ms.sendError(new MicroserviceError("testblabla", 32323));
		
		
	}

}
