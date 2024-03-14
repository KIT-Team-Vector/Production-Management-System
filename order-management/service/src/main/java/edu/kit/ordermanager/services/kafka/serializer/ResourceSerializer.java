package edu.kit.ordermanager.services.rest.kafka.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.kit.ordermanager.entities.Resource;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;
public class ResourceSerializer implements Serializer<Resource> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, Resource data) {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
        retVal = objectMapper.writeValueAsString(data).getBytes();
        } catch (Exception exception) {
        System.out.println("Error in serializing object"+ data);
        }
        return retVal;
    }

    @Override
    public void close() {

    }

}