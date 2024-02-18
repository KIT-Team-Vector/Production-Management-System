package edu.kit.pms.im.inventory;


import java.io.IOException;


import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.Context;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.BooleanDeserializer;
import org.apache.kafka.common.serialization.BooleanSerializer;
import org.apache.tomcat.util.scan.StandardJarScanner;

import edu.kit.pms.im.common.concepts.ResourceImpl;
import edu.kit.pms.im.common.concepts.ResourceSetImpl;
import edu.kit.pms.im.domain.Resource;
import edu.kit.pms.im.domain.ResourceSet;
import edu.kit.pms.im.domain.ResourceSetRepository;
import edu.kit.pms.im.domain.MicroserviceError;

import edu.kit.pms.im.common.services.MessageReceiverService;
import edu.kit.pms.im.database.*;

import edu.kit.pms.im.inventory.resourceLoader.WarLoader;
import edu.kit.pms.im.message.handlers.*;
import edu.kit.pms.im.message.kafka.IKafkaConstants;
import edu.kit.pms.im.message.kafka.clients.ConsumerFactory;
import edu.kit.pms.im.message.kafka.clients.ProducerFactory;
import edu.kit.pms.im.message.kafka.clients.SimpleConsumerFactory;
import edu.kit.pms.im.message.kafka.clients.SimpleProducerFactory;
import edu.kit.pms.im.message.serialization.MicroserviceErrorSerializer;
import edu.kit.pms.im.message.serialization.ResourceSetDeserializer;
import edu.kit.pms.im.message.serialization.ResourceSetSerializer;
import edu.kit.pms.im.message.services.*;
import edu.kit.pms.im.common.services.*;
import edu.kit.pms.im.common.controllers.*;

public class Main {

	public static void main(String[] args) {
		ResourceSet a = new ResourceSetImpl(null, 3);
		Resource b = new ResourceImpl(1, "ww");
		try {
			test();
			startAll();
			// testProducerKafkaUpdateRequest();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void test() {
		Thread serverThread = new Thread(() -> {
			// listen for rest requests
			ConsumerFactory<Boolean, BooleanDeserializer> consumerFactory = new SimpleConsumerFactory<Boolean, BooleanDeserializer>(
					BooleanDeserializer.class);
			org.apache.kafka.clients.consumer.Consumer<Long, Boolean> consumer = consumerFactory
					.create(IKafkaConstants.TOPIC_CHANGE_AMOUNT_OF_RESOURCE_FROM_INVENTORY_RESPONSE);
			while (true) {
				ConsumerRecords<Long, Boolean> t = consumer.poll(Duration.ofMillis(1000));
				t.forEach(record -> {
					System.out.println(record.value().booleanValue());

				});

			}
		});
		serverThread.start();
	}

	private void testDataBase() {
		ResourceSetRepository databaseConnector = new SqlResourceSetRepository();
		databaseConnector.getAll();
		databaseConnector.get(1);

		databaseConnector.updateAmount(1, 17);
	}

	private static void testProducerKafkaUpdateRequest() {
		Timer timer = new Timer();

		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Producer<Long, ResourceSetImpl> updateProducer = new SimpleProducerFactory<ResourceSetImpl, ResourceSetSerializer>(
						ResourceSetSerializer.class).create();
				ProducerRecord<Long, ResourceSetImpl> record = new ProducerRecord<Long, ResourceSetImpl>(
						IKafkaConstants.TOPIC_CHANGE_AMOUNT_OF_RESOURCE_FROM_INVENTORY, 1L,
						new ResourceSetImpl(new ResourceImpl(1, "Test"), 2));
				try {
					updateProducer.send(record).get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, 10000); // 3000 milliseconds delay (3 seconds)

	}

	private static void startAll() throws IOException {
		ResourceSetRepository resourceSetRepository = new SqlResourceSetRepository();
		InventoryManager inventoryManager = new InventoryManagerImpl(resourceSetRepository);

		ProducerFactory<MicroserviceError, MicroserviceErrorSerializer> errorProducer = new SimpleProducerFactory<MicroserviceError, MicroserviceErrorSerializer>(
				MicroserviceErrorSerializer.class);
		ProducerFactory<Boolean, BooleanSerializer> booleanProducer = new SimpleProducerFactory<Boolean, BooleanSerializer>(
				BooleanSerializer.class);
		MessageSenderService messageSenderService = new MessageSenderServiceImpl(errorProducer, booleanProducer);

		MessageAndRestController.init(inventoryManager, messageSenderService);
		ExternalInterfaceController endPointController = MessageAndRestController.getInstance();

		// listen and handle kafka requestsc
		ConsumerFactory<ResourceSetImpl, ResourceSetDeserializer> consumerFactory = new SimpleConsumerFactory<ResourceSetImpl, ResourceSetDeserializer>(
				ResourceSetDeserializer.class);
		MessageHandler changeAmountOfResourceMessageHandler = new ChangeAmountOfResourceHandler(endPointController,
				consumerFactory);
		MessageReceiverService ks = new MessageReceiverServiceImpl(changeAmountOfResourceMessageHandler);
		Thread thread = new Thread(ks);
		ClassLoader mainClassLoader = Main.class.getClassLoader();
		
		Thread serverThread = new Thread(() -> {
			// listen for rest requests
			try {
				// Create an instance of Tomcat
				Tomcat tomcat = new Tomcat();
				// Set the port number
				tomcat.setPort(8080);
				// specify workspace of tomcat instance
				tomcat.setBaseDir("temp");
				// root is set to host
				tomcat.getHost().setAppBase(".");
				tomcat.getConnector();
				// host url + contextPath => path to service
				String contextPath = "rest-service";
				// load web service war
				WarLoader loader = new WarLoader();
				// feed tomcat server with jakarta web service and context path
				Context ctx = tomcat.addWebapp(contextPath, loader.getWarFile().getCanonicalPath());
				// add dependecies of main application to jakarta web service (shared
				// dependencies)
				ctx.setParentClassLoader(mainClassLoader);
				// disable all scanns, all tomcat dependecies are already included in the
				// classpath of the jar
				StandardJarScanner jarScanner = (StandardJarScanner) ctx.getJarScanner();
				jarScanner.setScanAllDirectories(false);
				jarScanner.setScanClassPath(false);

				// gracefull shut down
				Runtime.getRuntime().addShutdownHook(new Thread(() -> {
					try {
						tomcat.stop();
					} catch (LifecycleException e) {
						e.printStackTrace();
					}
				}));
				// Start Tomcat
				tomcat.start();
				tomcat.getServer().await();
			} catch (LifecycleException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		});
		thread.start();
		serverThread.start();
	}

	public void kafka() {
		ResourceSetRepository resourceSetRepository = new SqlResourceSetRepository();
		InventoryManager inventoryManager = new InventoryManagerImpl(resourceSetRepository);

		ProducerFactory<MicroserviceError, MicroserviceErrorSerializer> errorProducer = new SimpleProducerFactory<MicroserviceError, MicroserviceErrorSerializer>(
				MicroserviceErrorSerializer.class);
		ProducerFactory<Boolean, BooleanSerializer> booleanProducer = new SimpleProducerFactory<Boolean, BooleanSerializer>(
				BooleanSerializer.class);
		MessageSenderService messageSenderService = new MessageSenderServiceImpl(errorProducer, booleanProducer);

		MessageAndRestController.init(inventoryManager, messageSenderService);
		ExternalInterfaceController endPointController = MessageAndRestController.getInstance();

		// listen and handle kafka requestsc
		ConsumerFactory<ResourceSetImpl, ResourceSetDeserializer> consumerFactory = new SimpleConsumerFactory<ResourceSetImpl, ResourceSetDeserializer>(
				ResourceSetDeserializer.class);
		MessageHandler changeAmountOfResourceMessageHandler = new ChangeAmountOfResourceHandler(endPointController,
				consumerFactory);
		MessageReceiverService ks = new MessageReceiverServiceImpl(changeAmountOfResourceMessageHandler);
		Thread thread = new Thread(ks);
		thread.start();

		// send kafka Request
		messageSenderService.sendChangeAmountOfResourceResponse(4L, true);
		messageSenderService.sendError(6L, new MicroserviceError("testblabla", "32323"));
	}

}
