package edu.kit.pms.mm.infrastructure.production.inventory.messageService;

import java.time.Duration;

public interface IKafkaConstants {

    String KAFKA_BROKERS = "134.3.17.150:9092";

    // unique
    String CLIENT_ID = "client1-id-machine-management";
    // unique
    String GROUP_ID_CONFIG = "group1-id-machine-management";

    String TOPIC_DECREASE_RESOURCE_SET_REQUEST = "decreaseResourceSetRequest";
    String TOPIC_DECREASE_RESOURCE_SET_RESPONSE = "decreaseResourceSetResponse";

    String OFFSET_RESET_LATEST = "latest";

    Integer MAX_POLL_RECORDS = 100;

    Duration POLLING_DURATION = Duration.ofMillis(100);
}
