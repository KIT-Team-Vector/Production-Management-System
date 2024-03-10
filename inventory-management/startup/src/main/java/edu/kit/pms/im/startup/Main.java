package edu.kit.pms.im.startup;

import org.apache.kafka.common.serialization.BooleanSerializer;

import edu.kit.pms.im.domain.ResourceSetRepository;
import edu.kit.pms.im.inventory.InventoryManager;
import edu.kit.pms.im.inventory.InventoryManagerImpl;
import edu.kit.pms.im.domain.InventoryManagementError;
import edu.kit.pms.im.domain.ResourceSetImpl;
import edu.kit.pms.im.common.services.MessageReceiverService;
import edu.kit.pms.im.database.*;
import edu.kit.pms.im.message.handlers.*;
import edu.kit.pms.im.message.kafka.clients.ConsumerFactory;
import edu.kit.pms.im.message.kafka.clients.ProducerFactory;
import edu.kit.pms.im.message.kafka.clients.SimpleConsumerFactory;
import edu.kit.pms.im.message.kafka.clients.SimpleProducerFactory;
import edu.kit.pms.im.message.serialization.MicroserviceErrorSerializer;
import edu.kit.pms.im.message.serialization.ResourceSetDeserializer;
import edu.kit.pms.im.message.services.*;
import edu.kit.pms.im.startup.tomcat.RestServiceCreator;
import edu.kit.pms.im.common.services.*;
import edu.kit.pms.im.common.controllers.*;

/**
 * 
 */
public class Main {

	public static void main(String[] args) {
		initialiseAndStartInventoryManagment();
	}

	private static void initialiseAndStartInventoryManagment() {

		InventoryManager inventoryManager = createInventoryManager();
		MessageSenderService messageSenderService = createMessageSenderService();
		InventoryControllerImpl.init(inventoryManager, messageSenderService);

		MessageReceiverService messageReceiverService = createMessageReceiverService();
		Thread messageReceiverThread = new Thread(messageReceiverService);

		Runnable restService = createRestService();
		Thread restServiceThread = new Thread(restService);

		messageReceiverThread.start();
		restServiceThread.start();
	}

	private static InventoryManager createInventoryManager() {
		ResourceSetRepository resourceSetRepository = new SqlResourceSetRepository();
		return new InventoryManagerImpl(resourceSetRepository);
	}

	private static MessageSenderService createMessageSenderService() {
		ProducerFactory<InventoryManagementError, MicroserviceErrorSerializer> errorProducer = new SimpleProducerFactory<InventoryManagementError, MicroserviceErrorSerializer>(
				MicroserviceErrorSerializer.class);
		ProducerFactory<Boolean, BooleanSerializer> booleanProducer = new SimpleProducerFactory<Boolean, BooleanSerializer>(
				BooleanSerializer.class);
		return new MessageSenderServiceImpl(errorProducer, booleanProducer);
	}

	private static MessageReceiverService createMessageReceiverService() {
		InventoryController inventoryController = InventoryControllerImpl.getInstance();

		// listen and handle kafka requestsc
		ConsumerFactory<ResourceSetImpl, ResourceSetDeserializer> consumerFactory = new SimpleConsumerFactory<ResourceSetImpl, ResourceSetDeserializer>(
				ResourceSetDeserializer.class);
		MessageHandler decreaseResourceSetHandler = new DecreaseResourceSetHandler(inventoryController,
				consumerFactory);
		return new MessageReceiverServiceImpl(decreaseResourceSetHandler);
	}

	private static Runnable createRestService() {
		ClassLoader mainClassLoader = Main.class.getClassLoader();
		return new RestServiceCreator().createRestService(mainClassLoader);
	};
}
