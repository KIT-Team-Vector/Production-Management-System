package kafka.clients;

import java.util.Collections;

import org.apache.kafka.clients.consumer.Consumer;

public abstract class ConsumerFactory <T, D> {
	
protected Class <D> deserializer;
	
	public ConsumerFactory(Class<D> deserializer) {
		this.deserializer = deserializer;
	}
	
	public Consumer<Long, T> create(String kafkaTopic) {
		Consumer <Long, T> consumer = createConsumer();
		consumer.subscribe(Collections.singletonList(kafkaTopic));
        return consumer;
    }
    protected abstract Consumer<Long, T> createConsumer();

}
