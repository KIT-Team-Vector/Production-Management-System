package edu.kit.pms.mm.infrastructure.production.inventory;

import edu.kit.pms.mm.app.GetInventory;
import edu.kit.pms.mm.core.Resource;
import edu.kit.pms.mm.core.ResourceSet;
import edu.kit.pms.mm.core.exceptions.InventoryException;
import edu.kit.pms.mm.infrastructure.production.coreImpl.ResourceImpl;
import edu.kit.pms.mm.infrastructure.production.coreImpl.ResourceSetImpl;
import edu.kit.pms.mm.infrastructure.production.inventory.messageService.ConsumerFactory;
import edu.kit.pms.mm.infrastructure.production.inventory.messageService.KafkaConstants;
import edu.kit.pms.mm.infrastructure.production.inventory.messageService.ProducerFactory;
import edu.kit.pms.mm.infrastructure.production.inventory.messageService.ResourceSetImplSerializer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.BooleanDeserializer;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class GetInventoryImpl implements GetInventory {

    private final Producer<Long, ResourceSet> msgProducer;
    private final ConsumerFactory<Boolean, BooleanDeserializer> consumerFactory;

    public GetInventoryImpl() {
        msgProducer = (new ProducerFactory<ResourceSet, ResourceSetImplSerializer>(ResourceSetImplSerializer.class)).create();
        consumerFactory = new ConsumerFactory<>(BooleanDeserializer.class);
    }

    public GetInventoryImpl(Producer<Long, ResourceSet> msgProducer, ConsumerFactory<Boolean, BooleanDeserializer> consumerFactory) {
        this.msgProducer = msgProducer;
        this.consumerFactory = consumerFactory;
    }

    @Override
    public ResourceSet get(ResourceSet resourceSet) throws InventoryException {
        long msgRecordKey = new Random().nextLong();
        ProducerRecord<Long, ResourceSet> msgRecord = new ProducerRecord<>(KafkaConstants.TOPIC_DECREASE_RESOURCE_SET_REQUEST, msgRecordKey, resourceSet);

        if (checkResponse(msgRecordKey, msgRecord)) {
            return resourceSet;
        } else {
            throw new InventoryException("Unable to get resources from inventory");
        }
    }

    @Override
    public ResourceSet get(Resource resource, int amount) throws InventoryException {
        return get(new ResourceSetImpl((ResourceImpl) resource, amount));
    }

    @Override
    public ResourceSet get(int resourceId, int amount) throws InventoryException {
        return get(new ResourceSetImpl(new ResourceImpl(resourceId), amount));
    }

    private boolean checkResponse(long expectedResponseKey, ProducerRecord<Long, ResourceSet> msgRecord) throws InventoryException {
        AtomicReference<Boolean> response = new AtomicReference<>();
        AtomicReference<Boolean> noMsgYet = new AtomicReference<>(true);
        boolean firstIteration = true;

        try (Consumer<Long, Boolean> msgConsumer = consumerFactory.create(KafkaConstants.TOPIC_DECREASE_RESOURCE_SET_RESPONSE)) {
            while (noMsgYet.get()) {
                ConsumerRecords<Long, Boolean> consumerRecords = msgConsumer.poll(KafkaConstants.POLLING_DURATION);
                // commits the offset of record to broker.
                msgConsumer.commitAsync();
                if (firstIteration) {
                    msgProducer.send(msgRecord).get();
                    firstIteration = false;
                }
                consumerRecords.forEach(record -> {
                    if (record.key().equals(expectedResponseKey)) {
                        response.set(record.value());
                        noMsgYet.set(false);
                    }
                });
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new InventoryException("Unable to send requests to inventory");
        }

        return response.get();
    }
}
