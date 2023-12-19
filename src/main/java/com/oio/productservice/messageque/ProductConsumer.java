package com.oio.productservice.messageque;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oio.productservice.dao.ProductRepository;
import com.oio.productservice.jpa.ProductEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductConsumer {

    private final ProductRepository productRepository;

    @KafkaListener(topics = "RENTED_PRODUCT")
    public void updateStatus(String kafkaMessage) {
        System.out.println(kafkaMessage);
        log.info("Kafka message ->" + kafkaMessage);

        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Long longProductNo = ((Integer)map.get("productNo")).longValue();
        Optional<ProductEntity> entity = productRepository.findById(longProductNo);
        ProductEntity productEntity = entity.orElseThrow();

        if (productEntity != null) {
            if (productEntity.getStatus() == 1 && ((String) map.get("status")).equals("대여완료")) {
                productEntity.updateStatus(0);
                productEntity.updateRentCount(1);
            } else if (productEntity.getStatus() == 0 && ((String) map.get("status")).equals("대여중")) {
                productEntity.updateStatus(1);
            }
            productRepository.save(productEntity);
        }
    }

}
