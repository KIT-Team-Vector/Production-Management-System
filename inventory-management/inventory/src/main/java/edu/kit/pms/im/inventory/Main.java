package edu.kit.pms.im.inventory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.kafka.common.serialization.BooleanSerializer;

import edu.kit.pms.im.database.*;
import edu.kit.pms.im.domain.*;
import edu.kit.pms.im.inventory.resourceLoader.WarLoader;
import edu.kit.pms.im.message.handlers.*;
import edu.kit.pms.im.message.kafka.clients.ConsumerFactory;
import edu.kit.pms.im.message.kafka.clients.ProducerFactory;
import edu.kit.pms.im.message.kafka.clients.SimpleConsumerFactory;
import edu.kit.pms.im.message.kafka.clients.SimpleProducerFactory;
import edu.kit.pms.im.message.serialization.MicroserviceErrorSerializer;
import edu.kit.pms.im.message.serialization.RessourceSetDeserializer;
import edu.kit.pms.im.message.services.*;

public class Main {

	public static void main(String[] args) {

		try {
			testTomcat();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void testTomcat() throws IOException {

		try {
			// Create an instance of Tomcat
			Tomcat tomcat = new Tomcat();
			tomcat.setPort(8080); // Set the port number

			File warfilee = new File("../rest-service/target/rest-service.war");
			WarLoader loader = new WarLoader();
			warfilee  = loader.getWarFile();
			System.out.println(warfilee.getCanonicalPath());
			String contextPath = "rest-service"; // Context path for your app
													// with
			tomcat.setBaseDir("temp"); // your
			// actual
			// path

			tomcat.getHost().setAppBase(".");
			tomcat.getConnector();
			tomcat.addWebapp(contextPath, warfilee.getCanonicalPath());
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
		}
	}

	

	private void testDataBase() {
		ResourceSetRepository databaseConnector = new SqlResourceSetRepository();
		databaseConnector.getAll();
		databaseConnector.get(1);

		databaseConnector.updateAmount(1, 17);
	}

	private void testKafka() {

		// listen and handle kafka requestsc
		ConsumerFactory<ResourceSet, RessourceSetDeserializer> consumerFactory = new SimpleConsumerFactory<ResourceSet, RessourceSetDeserializer>(
				RessourceSetDeserializer.class);
		MessageHandler removeMessageHandler = new RemoveResourcesMessageHandler(consumerFactory);
		MessageReceiverService ks = new MessageReceiverServiceImpl(removeMessageHandler);
		Thread thread = new Thread(ks);
		thread.start();

		// send kafka Request
		ProducerFactory<MicroserviceError, MicroserviceErrorSerializer> errorProducer = new SimpleProducerFactory<MicroserviceError, MicroserviceErrorSerializer>(
				MicroserviceErrorSerializer.class);
		ProducerFactory<Boolean, BooleanSerializer> booleanProducer = new SimpleProducerFactory<Boolean, BooleanSerializer>(
				BooleanSerializer.class);
		MessageSenderService ms = new MessageSenderServiceImpl(errorProducer, booleanProducer);
		ms.sendDeleteFromInventoryResponse(false);
		ms.sendError(new MicroserviceError("testblabla", 32323));

	}

}
