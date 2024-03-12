package edu.kit.pms.mm.infrastructure.production.inventory.messageService;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.kit.pms.mm.infrastructure.production.coreImpl.ResourceSetImpl;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class ResourceSetImplSerializer implements Serializer<ResourceSetImpl> {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, ResourceSetImpl data) {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            retVal = objectMapper.writeValueAsString(data).getBytes();
        } catch (Exception exception) {
            LOGGER.error("Error in serializing object" + data);
        }
        return retVal;
    }

    @Override
    public void close() {
    }

}
