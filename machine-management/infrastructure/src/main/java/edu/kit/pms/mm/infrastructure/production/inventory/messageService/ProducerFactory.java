package edu.kit.pms.mm.infrastructure.production.inventory.messageService;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class ProducerFactory<T, D> {

    private static final Logger LOGGER = LogManager.getLogger();

    protected Class<D> serializer;

    public ProducerFactory(Class<D> serializer) {
        this.serializer = serializer;
    }

    public Producer<Long, T> create() {
        return createProducer();
    }

    private Producer<Long, T> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv("KAFKA_BROKER_HOST") + ":" + System.getenv("KAFKA_BROKER_PORT"));
        props.put(ProducerConfig.CLIENT_ID_CONFIG, KafkaConstants.CLIENT_ID);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, serializer.getName());

        LOGGER.info("Created new KafkaProducer");
        return new KafkaProducer<>(props);
    }

}
