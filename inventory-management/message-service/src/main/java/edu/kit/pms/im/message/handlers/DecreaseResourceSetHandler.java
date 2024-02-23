package edu.kit.pms.im.message.handlers;

import org.apache.kafka.clients.consumer.Consumer;

import org.apache.kafka.clients.consumer.ConsumerRecords;

import edu.kit.pms.im.common.concepts.ResourceSetImpl;
import edu.kit.pms.im.common.controllers.ExternalInterfaceController;
import edu.kit.pms.im.message.kafka.IKafkaConstants;
import edu.kit.pms.im.message.kafka.clients.ConsumerFactory;
import edu.kit.pms.im.message.serialization.ResourceSetDeserializer;

public class ChangeAmountOfResourceHandler implements MessageHandler {

	private ConsumerFactory<ResourceSetImpl, ResourceSetDeserializer> consumerFactory;
	private ExternalInterfaceController controller;
	private Consumer<Long, ResourceSetImpl> consumer;

	public ChangeAmountOfResourceHandler(ExternalInterfaceController controller, ConsumerFactory<ResourceSetImpl, ResourceSetDeserializer> consumerFactory) {
		this.controller = controller;
		this.consumerFactory = consumerFactory;
	}

	@Override
	public void handleMessages() throws Error {
		if (consumer == null) {
			this.allocateResources();
		}
		ConsumerRecords<Long, ResourceSetImpl> consumerRecords = consumer.poll(IKafkaConstants.POLLING_DURATION);
		consumerRecords.forEach(record -> {
			// handle the request
			controller.changeAmountOfResource(record.key(), record.value());
		});
		// commits the offset of record to broker.
		consumer.commitAsync();
	}

	@Override
	public void close() {
		consumer.close();

	}

	@Override
	public MessageHandler allocateResources() {
		consumer = consumerFactory.create(IKafkaConstants.TOPIC_CHANGE_AMOUNT_OF_RESOURCE_FROM_INVENTORY);
		return this;
	}

}
