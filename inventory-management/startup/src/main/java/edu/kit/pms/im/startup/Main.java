package edu.kit.pms.im.startup;

import org.apache.kafka.common.serialization.BooleanSerializer;

import edu.kit.pms.im.domain.ResourceSetRepository;
import edu.kit.pms.im.domain.InventoryManagementError;
import edu.kit.pms.im.domain.ResourceSetImpl;
import edu.kit.pms.im.common.services.MessageReceiverService;
import edu.kit.pms.im.database.*;
import edu.kit.pms.im.inventory.InventoryManager;
import edu.kit.pms.im.inventory.InventoryManagerImpl;
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

		// sendTestRequests();
		// receiveTestRequests();
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

//	private static void sendTestRequests() { Timer timer = new Timer();
//	  
//	  timer.schedule(new TimerTask() {
//	  
//	  @Override public void run() { Producer<Long, ResourceSetImpl> updateProducer
//	            = new SimpleProducerFactory<ResourceSetImpl,
//	           ResourceSetSerializer>( ResourceSetSerializer.class).create();
//	            ProducerRecord<Long, ResourceSetImpl> record = new
//	            ProducerRecord<Long, ResourceSetImpl>(
//	            IKafkaConstants.TOPIC_DECREASE_RESOURCE_SET_REQUEST, 1L, new
//	            ResourceSetImpl(new ResourceImpl(11, "Test"), 1000)); try {
//	            updateProducer.send(record).get(); } catch (InterruptedException e)
//	            { // TODO Auto-generated catch block e.printStackTrace(); } catch
//	             e.printStackTrace(); } } }, 10000); // 3000 milliseconds delay (3
//	          seconds)
//	 
//	            }
//
//	private static void receiveTestRequests() {
//		Thread serverThread = new Thread(() -> { // listen for rest requests
//			ConsumerFactory<Boolean, BooleanDeserializer> consumerFactory = new SimpleConsumerFactory<Boolean, BooleanDeserializer>(
//					BooleanDeserializer.class);
//			org.apache.kafka.clients.consumer.Consumer<Long, Boolean> consumer = consumerFactory
//					.create(IKafkaConstants.TOPIC_DECREASE_RESOURCE_SET_RESPONSE);
//			while (true) {
//				ConsumerRecords<Long, Boolean> t = consumer.poll(Duration.ofMillis(1000));
//				t.forEach(record -> {
//					System.out.println(record.value().booleanValue());
//
//				});
//
//			}
//		});
//		serverThread.start();
//	}

	/**
	 * private static void startAll() throws IOException { ResourceSetRepository
	 * resourceSetRepository = new SqlResourceSetRepository(); InventoryManager
	 * inventoryManager = new InventoryManagerImpl(resourceSetRepository);
	 * 
	 * ProducerFactory<MicroserviceError, MicroserviceErrorSerializer> errorProducer
	 * = new SimpleProducerFactory<MicroserviceError, MicroserviceErrorSerializer>(
	 * MicroserviceErrorSerializer.class); ProducerFactory<Boolean,
	 * BooleanSerializer> booleanProducer = new SimpleProducerFactory<Boolean,
	 * BooleanSerializer>( BooleanSerializer.class); MessageSenderService
	 * messageSenderService = new MessageSenderServiceImpl(errorProducer,
	 * booleanProducer);
	 * 
	 * InventoryControllerImpl.init(inventoryManager, messageSenderService);
	 * InventoryController endPointController =
	 * InventoryControllerImpl.getInstance();
	 * 
	 * // listen and handle kafka requestsc ConsumerFactory<ResourceSetImpl,
	 * ResourceSetDeserializer> consumerFactory = new
	 * SimpleConsumerFactory<ResourceSetImpl, ResourceSetDeserializer>(
	 * ResourceSetDeserializer.class); MessageHandler
	 * changeAmountOfResourceMessageHandler = new
	 * DecreaseResourceSetHandler(endPointController, consumerFactory);
	 * MessageReceiverService ks = new
	 * MessageReceiverServiceImpl(changeAmountOfResourceMessageHandler); Thread
	 * thread = new Thread(ks); ClassLoader mainClassLoader =
	 * Main.class.getClassLoader();
	 * 
	 * Thread serverThread = new Thread(() -> { // listen for rest requests try { //
	 * Create an instance of Tomcat Tomcat tomcat = new Tomcat(); // Set the port
	 * number tomcat.setPort(8080); // specify workspace of tomcat instance
	 * tomcat.setBaseDir("temp"); // root is set to host
	 * tomcat.getHost().setAppBase("."); tomcat.getConnector(); // host url +
	 * contextPath => path to service String contextPath = "rest-service"; // load
	 * web service war WarLoader loader = new WarLoader(); // feed tomcat server
	 * with jakarta web service and context path Context ctx =
	 * tomcat.addWebapp(contextPath, loader.getWarFile().getCanonicalPath()); // add
	 * dependecies of main application to jakarta web service with main class //
	 * loader (shared // dependencies) ctx.setParentClassLoader(mainClassLoader); //
	 * disable all scanns, all tomcat dependecies are already included in the //
	 * classpath of the jar StandardJarScanner jarScanner = (StandardJarScanner)
	 * ctx.getJarScanner(); jarScanner.setScanAllDirectories(false);
	 * jarScanner.setScanClassPath(false);
	 * 
	 * // gracefull shut down Runtime.getRuntime().addShutdownHook(new Thread(() ->
	 * { try { tomcat.stop(); } catch (LifecycleException e) { e.printStackTrace();
	 * } })); // Start Tomcat tomcat.start(); tomcat.getServer().await(); } catch
	 * (LifecycleException e) { e.printStackTrace(); } catch
	 * (IllegalArgumentException e) { e.printStackTrace(); } catch (IOException e1)
	 * { // TODO Auto-generated catch block e1.printStackTrace(); }
	 * 
	 * }); thread.start(); serverThread.start(); }
	 **/

}
