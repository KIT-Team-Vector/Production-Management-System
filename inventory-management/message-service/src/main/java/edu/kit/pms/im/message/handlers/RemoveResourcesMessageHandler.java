package edu.kit.pms.im.message.handlers;

import org.apache.kafka.clients.consumer.Consumer;

import org.apache.kafka.clients.consumer.ConsumerRecords;

import edu.kit.pms.im.domain.ResourceSet;
import edu.kit.pms.im.message.kafka.IKafkaConstants;
import edu.kit.pms.im.message.kafka.clients.ConsumerFactory;
import edu.kit.pms.im.message.serialization.RessourceSetDeserializer;

public class RemoveResourcesMessageHandler implements MessageHandler {

	private ConsumerFactory<ResourceSet, RessourceSetDeserializer> consumerFactory;
	private Consumer<Long, ResourceSet> consumer;

	public RemoveResourcesMessageHandler(ConsumerFactory<ResourceSet, RessourceSetDeserializer> consumerFactory) {
		this.consumerFactory = consumerFactory;
	}

	@Override
	public void handleMessages() throws Error {
		if (consumer == null) {
			this.allocateRessources();
		}
		ConsumerRecords<Long, ResourceSet> consumerRecords = consumer.poll(IKafkaConstants.POLLING_DURATION);
		// print each record.
		consumerRecords.forEach(record -> {
			System.out.println("Record Key " + record.key());
			System.out.println("Record value: " + record.value().getRessource().getName() + " "
					+ record.value().getRessource().getId() + " " + record.value().getAmount());
			System.out.println("Record partition " + record.partition());
			System.out.println("Record offset " + record.offset());
		});
		// commits the offset of record to broker.
		consumer.commitAsync();
		// ToDO handle the request
	}

	@Override
	public void close() {
		consumer.close();

	}

	@Override
	public MessageHandler allocateRessources() {
		consumer = consumerFactory.create(IKafkaConstants.TOPIC_REMOVE_FROM_INVENTORY);
		return this;
	}

}
