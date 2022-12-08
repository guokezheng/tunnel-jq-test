package com.tunnel.platform.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dzy
 * @date 2022/9/9 11:26
 */
//@Configuration
public class KafkaConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String servers;
    @Value("${spring.kafka.consumer.value-deserializer}")
    private String valueDeserializer;
    @Value("${spring.kafka.consumer.key-deserializer}")
    private String keyDeserializer;
    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String offset;
    @Value("${spring.kafka.consumer.enable-auto-commit}")
    private String zutoCommit;
    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;


    public ConsumerFactory<Object, Object> consumerFactory() {
        Map<String, Object> consumerProperties = new HashMap<>();
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,offset);
        consumerProperties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, zutoCommit);
        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        consumerProperties.put("session.timeout.ms", 3000000);
//        consumerProperties.put("security.protocol", "SASL_SSL");
//        consumerProperties.put(SaslConfigs.SASL_MECHANISM, "SCRAM-SHA-256");
//        consumerProperties.put(SaslConfigs.SASL_JAAS_CONFIG, "org.apache.kafka.common.security.scram.ScramLoginModule required username='jq_tunnel' password='Sdhsg2021'");
        return new DefaultKafkaConsumerFactory<>(consumerProperties);
    }

    @Bean("myKafkaContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(
            ConcurrentKafkaListenerContainerFactoryConfigurer configurer) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        configurer.configure(factory, consumerFactory());
        return factory;
    }
}
