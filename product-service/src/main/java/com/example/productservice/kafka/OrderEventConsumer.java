package com.example.productservice.kafka;
import com.example.common.event.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
@Component
@Slf4j
public class OrderEventConsumer {
    @KafkaListener(topics = "order-topic", groupId = "product-consumer-group")
    public void consumeOrderEvent(OrderCreatedEvent event) {
        log.info("Kafka 수신 - 주문ID: {}, 상품: {}, 수량: {}",
                event.getOrderId(), event.getProductId(), event.getQuantity());
    }
}