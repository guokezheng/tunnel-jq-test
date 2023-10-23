package com.tunnel.platform.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfigTwo {

    @Value("${spring.kafka.wulian.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${spring.kafka.wulian.consumer.enable-auto-commit}")
    private boolean enableAutoCommit;
    @Value("${spring.kafka.wulian.consumer.group-id}")
    private String groupId;
    @Value("${spring.kafka.wulian.consumer.auto-offset-reset}")
    private String offset;
    @Value("${spring.kafka.wulian.consumer.max-poll-records}")
    private String maxPollRecordes;
//    @Value("${spring.kafka.wulian.producer.linger-ms}")
//    private Integer lingerMs;
//    @Value("${spring.kafka.wulian.producer.max-request-size}")
//    private Integer maxRequestSize;
//    @Value("${spring.kafka.wulian.producer.batch-size}")
//    private Integer batchSize;
//    @Value("${spring.kafka.wulian.producer.buffer-memory}")
//    private Integer bufferMemory;


    @Value("${spring.kafka.wulian.producer.security.protocol}")
    private String producerSecurityProtocol;
    @Value("${spring.kafka.wulian.producer.security.properties.sasl.mechanism}")
    private String producerSecuritySaslMechanism;
    @Value("${spring.kafka.wulian.producer.security.properties.sasl.jaas.config}")
    private String producerSecuritySaslJaas;

    @Value("${spring.kafka.wulian.consumer.security.protocol}")
    private String consumerSecurityProtocol;
    @Value("${spring.kafka.wulian.consumer.security.properties.sasl.mechanism}")
    private String consumerSecuritySaslMechanism;
    @Value("${spring.kafka.wulian.consumer.security.properties.sasl.jaas.config}")
    private String consumerSecuritySaslJaas;


    @Bean
    public KafkaTemplate<String, String> kafkaTwoTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Integer, String>> kafkaTwoContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Integer, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(3);
        factory.getContainerProperties().setPollTimeout(3000);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }
    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Integer, String>> kafkaTwoContainerFactoryTwo() {
        ConcurrentKafkaListenerContainerFactory<Integer, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryTwo());
        factory.setConcurrency(3);
        factory.setBatchListener(true);
        factory.getContainerProperties().setPollTimeout(3000);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }


    private ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    public ConsumerFactory<Integer, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    public ConsumerFactory<Integer, String> consumerFactoryTwo() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    private Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put("session.timeout.ms", 3000000);
        props.put("security.protocol", producerSecurityProtocol);
        props.put(SaslConfigs.SASL_MECHANISM, producerSecuritySaslMechanism);
        props.put(SaslConfigs.SASL_JAAS_CONFIG, producerSecuritySaslJaas);
        return props;
    }

    private Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, offset);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        props.put(ConsumerConfig.GROUP_ID_CONFIG,groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put("max.poll.records", maxPollRecordes);
        props.put("session.timeout.ms", 30000);
        props.put("security.protocol", consumerSecurityProtocol);
        props.put(SaslConfigs.SASL_MECHANISM, consumerSecuritySaslMechanism);
        props.put(SaslConfigs.SASL_JAAS_CONFIG, consumerSecuritySaslJaas);
        return props;
    }
}

