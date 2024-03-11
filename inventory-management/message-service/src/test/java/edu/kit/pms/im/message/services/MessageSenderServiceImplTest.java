package edu.kit.pms.im.message.services;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.BooleanSerializer;
import org.junit.Before;
import org.junit.Test;

import edu.kit.pms.im.domain.InventoryManagementError;
import edu.kit.pms.im.message.kafka.IKafkaConstants;
import edu.kit.pms.im.message.kafka.clients.ProducerFactory;
import edu.kit.pms.im.message.serialization.MicroserviceErrorSerializer;

public class MessageSenderServiceImplTest {
	private MessageSenderServiceImpl messageSenderService;
    private ProducerFactory<InventoryManagementError, MicroserviceErrorSerializer> mockErrorProducerFactory;
    private ProducerFactory<Boolean, BooleanSerializer> mockBooleanProducerFactory;
    private Producer<Long, InventoryManagementError> mockErrorProducer;
    private Producer<Long, Boolean> mockBooleanProducer;
    private Future<RecordMetadata> mockErrorFuture;
    private Future<RecordMetadata> mockBooleanFuture;

    @SuppressWarnings("unchecked")
	@Before
    public void setUp() {
        mockErrorProducerFactory = mock(ProducerFactory.class);
        mockBooleanProducerFactory = mock(ProducerFactory.class);
        mockErrorProducer = mock(Producer.class);
        mockBooleanProducer = mock(Producer.class);
        mockErrorFuture = mock(Future.class);
        mockBooleanFuture = mock(Future.class);
        when(mockErrorProducerFactory.create()).thenReturn(mockErrorProducer);
        when(mockBooleanProducerFactory.create()).thenReturn(mockBooleanProducer);
        messageSenderService = new MessageSenderServiceImpl(mockErrorProducerFactory, mockBooleanProducerFactory);
    }

    @Test
    public void testSendError() throws InterruptedException, ExecutionException {
    	// Given
    	Long key = 1L;
        InventoryManagementError error = new InventoryManagementError("type", "Test error", 1111);
        ProducerRecord<Long, InventoryManagementError> record = new ProducerRecord<>(
                IKafkaConstants.TOPIC_Error, key, error);
        when(mockErrorProducer.send(record)).thenReturn(mockErrorFuture);

        // When
        messageSenderService.sendError(key, error);

        // Then
        verify(mockErrorProducer).send(record);
        verify(mockErrorFuture).get();
    }

    @Test
    public void testSendDecreaseResourceSetResponse() throws InterruptedException, ExecutionException {
    	// Given
        Long key = 1L;
        Boolean success = true;
        ProducerRecord<Long, Boolean> record = new ProducerRecord<>(
                IKafkaConstants.TOPIC_DECREASE_RESOURCE_SET_RESPONSE, key, success);
        when(mockBooleanProducer.send(record)).thenReturn(mockBooleanFuture);

        // When
        messageSenderService.sendDecreaseResourceSetResponse(key, success);

        // Then
        verify(mockBooleanProducer).send(record);
        verify(mockBooleanFuture).get();
    }

    @Test
    public void testStop() {
        // When
        messageSenderService.stop();

        // Then
        verify(mockErrorProducer).flush();
        verify(mockErrorProducer).close();
        verify(mockBooleanProducer).flush();
        verify(mockBooleanProducer).close();
    }
}
