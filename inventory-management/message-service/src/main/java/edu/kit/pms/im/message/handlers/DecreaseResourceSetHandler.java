package edu.kit.pms.im.message.handlers;

import org.apache.kafka.clients.consumer.Consumer;

import org.apache.kafka.clients.consumer.ConsumerRecords;

import edu.kit.pms.im.common.controllers.InventoryController;
import edu.kit.pms.im.domain.ResourceSetImpl;
import edu.kit.pms.im.message.kafka.IKafkaConstants;
import edu.kit.pms.im.message.kafka.clients.ConsumerFactory;
import edu.kit.pms.im.message.serialization.ResourceSetDeserializer;

public class DecreaseResourceSetHandler implements MessageHandler {

	private ConsumerFactory<ResourceSetImpl, ResourceSetDeserializer> consumerFactory;
	private InventoryController controller;
	private Consumer<Long, ResourceSetImpl> consumer;

	public DecreaseResourceSetHandler(InventoryController controller, ConsumerFactory<ResourceSetImpl, ResourceSetDeserializer> consumerFactory) {
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
			controller.decreaseResourceSet(record.key(), record.value());
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
		consumer = consumerFactory.create(IKafkaConstants.TOPIC_DECREASE_RESOURCE_SET_REQUEST);
		return this;
	}

}
