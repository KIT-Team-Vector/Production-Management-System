package serialization;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import domain.Ressource;


public class RessourceDeserializer implements Deserializer<Ressource> {

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
	}

	@Override
	public Ressource deserialize(String topic, byte[] data) {
		ObjectMapper mapper = new ObjectMapper();
		Ressource object = null;
		try {
			object = mapper.readValue(data, Ressource.class);
		} catch (Exception exception) {
			System.out.println("Error in deserializing bytes " + exception);
		}
		return object;
	}

	@Override
	public void close() {
	}

}
