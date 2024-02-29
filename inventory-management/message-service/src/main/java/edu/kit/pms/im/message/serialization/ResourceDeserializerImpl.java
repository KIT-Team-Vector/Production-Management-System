package edu.kit.pms.im.message.serialization;

import java.util.Map;



import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.kit.pms.im.common.concepts.ResourceImpl;


public class ResourceDeserializerImpl implements Deserializer<ResourceImpl> {

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
	}

	@Override
	public ResourceImpl deserialize(String topic, byte[] data) {
		ObjectMapper mapper = new ObjectMapper();
		ResourceImpl object = null;
		try {
			object = mapper.readValue(data, ResourceImpl.class);
		} catch (Exception exception) {
			System.out.println("Error in deserializing bytes " + exception);
		}
		return object;
	}

	@Override
	public void close() {
	}

}
