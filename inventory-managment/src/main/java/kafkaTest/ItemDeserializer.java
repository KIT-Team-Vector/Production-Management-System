package kafkaTest;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;


public class ItemDeserializer implements Deserializer<Item> {

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
	}

	@Override
	public Item deserialize(String topic, byte[] data) {
		ObjectMapper mapper = new ObjectMapper();
		Item object = null;
		try {
			object = mapper.readValue(data, Item.class);
		} catch (Exception exception) {
			System.out.println("Error in deserializing bytes " + exception);
		}
		return object;
	}

	@Override
	public void close() {
	}

}
