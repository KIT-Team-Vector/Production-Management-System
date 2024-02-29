package edu.kit.pms.im.message.kafka.clients;

import org.apache.kafka.clients.producer.Producer;

public abstract class ProducerFactory<T, D> {
	
protected Class <D> serializer;
	
	public ProducerFactory(Class<D> serializer) {
		this.serializer = serializer;
	}
	
	public Producer<Long, T> create() {
		Producer<Long, T> producer = createProducer();
        return producer;
    }
    protected abstract Producer<Long, T> createProducer();

}

