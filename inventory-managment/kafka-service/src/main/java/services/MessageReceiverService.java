package kafka;

import java.util.concurrent.ExecutionException;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import domain.RessourceSet;
import serialization.RessourceSetDeserializer;
import serialization.RessourceSetSerializer;

public class KafkaServer implements IKafkaServer{
	
	private boolean isRunning;

	public KafkaServer() {
		isRunning = false;
	}
	
	public boolean isRunning() {
		return isRunning;
	}

	public void start() {
		isRunning = true;
		runConsumer();
	}
	
	public void stop() {
		isRunning = true;
	}
	
	public void sendRessourceSet(RessourceSet ressourceSet) {
		runProducer(ressourceSet);
	}

	private void runConsumer() {
		ConsumerCreator<RessourceSet, RessourceSetDeserializer> consumerCreator = new ConsumerCreator<RessourceSet, RessourceSetDeserializer>(
				RessourceSetDeserializer.class);

		try (Consumer<Long, RessourceSet> consumer = consumerCreator.createConsumer(IKafkaConstants.TOPIC_INVENTORY)) {
			while (isRunning) {
				ConsumerRecords<Long, RessourceSet> consumerRecords = consumer.poll(IKafkaConstants.POLLING_DURATION);
				// print each record.
				consumerRecords.forEach(record -> {
					System.out.println("Record Key " + record.key());
					System.out.println("Record value: " + record.value().getRessource().getName() + " " + record.value().getRessource().getId() + " " + record.value().getAmount());
					System.out.println("Record partition " + record.partition());
					System.out.println("Record offset " + record.offset());
				});
				// commits the offset of record to broker.
				consumer.commitAsync();
				//ToDO do the request
			}	
		}
	}

	private void runProducer(RessourceSet ressourceSet) {
		ProducerCreator<RessourceSet, RessourceSetSerializer> producerCreator = new ProducerCreator<RessourceSet, RessourceSetSerializer>(RessourceSetSerializer.class);
		Producer<Long, RessourceSet> producer = producerCreator.createProducer();

		ProducerRecord<Long, RessourceSet> record = new ProducerRecord<Long, RessourceSet>(IKafkaConstants.TOPIC_INVENTORY, 1L,
				ressourceSet);
		try {
			RecordMetadata metadata = producer.send(record).get();
			System.out.println("Record sent with key " + "to partition " + metadata.partition() + " with offset "
					+ metadata.offset());
		} catch (ExecutionException e) {
			System.out.println("Error in sending record");
			System.out.println(e);
		} catch (InterruptedException e) {
			System.out.println("Error in sending record");
			System.out.println(e);
		}
	}
}
