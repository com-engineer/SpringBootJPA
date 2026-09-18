package com.example.order.kafka;

import com.example.order.entity.Order;
import com.example.order.event.OrderCreatedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
@RequiredArgsConstructor
@Service
@Slf4j
public class OrderEventPublisher {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String,String> kafkaTemplate;

    @Value("${kafka.topic.order-events}")
    private String orderEventsTopic;

    public void publishOrderCreated(Order order) {
        OrderCreatedEvent event = OrderCreatedEvent.from(order);
        try{
            //convert an event object into JSON
            String payload = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(orderEventsTopic,String.valueOf(order.getId()),payload);
            log.info("Published ORDER_CREATED to kafka: orderId={}",order.getId());
        } catch (JsonProcessingException ex) {
            log.error("Failed to serialize order event for orderId={}", order.getId(), ex);
        }
    }
}
