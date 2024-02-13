package edu.kit.pms.im.message.services;

import java.util.concurrent.ExecutionException;


import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.BooleanSerializer;

import edu.kit.pms.im.domain.MicroserviceError;
import edu.kit.pms.im.message.kafka.IKafkaConstants;
import edu.kit.pms.im.message.kafka.clients.ProducerFactory;
import edu.kit.pms.im.message.kafka.clients.SimpleProducerFactory;
import edu.kit.pms.im.message.serialization.MicroserviceErrorSerializer;

public class MessageSenderServiceImpl implements MessageSenderService {
	
	private Producer<Long, MicroserviceError> errorProducer;
	private Producer<Long, Boolean> booleanProducer;

	public MessageSenderServiceImpl(
			ProducerFactory<MicroserviceError, MicroserviceErrorSerializer> errorProducerFactory,
			ProducerFactory<Boolean, BooleanSerializer> booleanProducerFactory) {
		
		errorProducer = errorProducerFactory.create();
		booleanProducer = booleanProducerFactory.create();
	}

	@Override
	public void sendError(MicroserviceError mError) {

		ProducerRecord<Long, MicroserviceError> record = new ProducerRecord<Long, MicroserviceError>(
				IKafkaConstants.TOPIC_REMOVE_FROM_INVENTORY, 1L, mError);

		try {
			// producer is thread safe
			errorProducer.send(record).get();
		} catch (ExecutionException e) {
			System.out.println("Error in sending record");
			System.out.println(e);
			endProducer(errorProducer);
		} catch (InterruptedException e) {
			System.out.println("Error in sending record");
			System.out.println(e);
			endProducer(errorProducer);
		}

	}

	@Override
	public void sendDeleteFromInventoryResponse(Boolean success) {
		
		ProducerRecord<Long, Boolean> record = new ProducerRecord<Long, Boolean>(
				IKafkaConstants.TOPIC_REMOVE_FROM_INVENTORY, 1L, success);

		try {
			// producer is thread safe
			booleanProducer.send(record).get();
		} catch (ExecutionException e) {
			System.out.println("Error in sending record");
			System.out.println(e);
			errorProducer.flush();
			errorProducer.close();
		} catch (InterruptedException e) {
			System.out.println("Error in sending record");
			System.out.println(e);
			endProducer(booleanProducer);
		}

	}

	@Override
	public void stop() {
		endProducer(errorProducer);
		endProducer(booleanProducer);
	}
	
	private void endProducer(Producer<Long, ?> producer) {
		if (producer != null) {
			producer.flush();
			producer.close();
		}
	}
}
