package edu.kit.pms.im.message.serialization;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.kit.pms.im.domain.MicroserviceError;

public class MicroserviceErrorDeserializer implements Deserializer<MicroserviceError> {
	
	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
	}

	@Override
	public MicroserviceError deserialize(String topic, byte[] data) {
		ObjectMapper mapper = new ObjectMapper();
		MicroserviceError object = null;
		try {
			object = mapper.readValue(data, MicroserviceError.class);
		} catch (Exception exception) {
			System.out.println("Error in deserializing bytes " + exception);
		}
		return object;
	}

	@Override
	public void close() {
	}

}
