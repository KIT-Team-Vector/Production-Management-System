package serialization;

import java.util.Map;


import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.RessourceSet;


public class RessourceSetDeserializer implements Deserializer<RessourceSet> {

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
	}

	@Override
	public RessourceSet deserialize(String topic, byte[] data) {
		ObjectMapper mapper = new ObjectMapper();
		RessourceSet object = null;
		try {
			object = mapper.readValue(data, RessourceSet.class);
		} catch (Exception exception) {
			System.out.println("Error in deserializing bytes " + exception);
		}
		return object;
	}

	@Override
	public void close() {
	}

}
