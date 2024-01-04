package kafkaTest;
import java.util.concurrent.ExecutionException;


import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class App {
	public static void main(String[] args) {
		runProducer();
		runConsumer();
	}

	static void runConsumer() {
		Consumer<Long, Item> consumer = ConsumerCreator.createConsumer();

		int noMessageFound = 0;

		while (true) {
			ConsumerRecords<Long, Item> consumerRecords = consumer.poll(IKafkaConstants.POLLING_DURATION);
			if (consumerRecords.count() == 0) {
				noMessageFound++;
				if (noMessageFound > IKafkaConstants.MAX_NO_MESSAGE_FOUND_COUNT)
	                // If no message found count is reached to threshold exit loop.  
	                break;
	              else
	                  continue;
	          }
	

			// print each record.
			consumerRecords.forEach(record -> {
				System.out.println("Record Key " + record.key());
				System.out.println("Record value " + record.value().getName());
				System.out.println("Record partition " + record.partition());
				System.out.println("Record offset " + record.offset());
			});

			// commits the offset of record to broker.
			consumer.commitAsync();
		}
		consumer.close();
	}

	static void runProducer() {
		Producer<Long, Item> producer = ProducerCreator.createProducer();

			ProducerRecord<Long, Item> record = new ProducerRecord<Long, Item>(IKafkaConstants.TOPIC_NAME,
					new Item("iteam4","4", "50"));
			try {
				RecordMetadata metadata = producer.send(record).get();
				System.out.println("Record sent with key " + " to partition " + metadata.partition()
						+ " with offset " + metadata.offset());
			} catch (ExecutionException e) {
				System.out.println("Error in sending record");
				System.out.println(e);
			} catch (InterruptedException e) {
				System.out.println("Error in sending record");
				System.out.println(e);
			}
	}
}
