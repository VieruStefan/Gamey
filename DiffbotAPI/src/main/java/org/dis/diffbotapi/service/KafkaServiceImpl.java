package org.dis.diffbotapi.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaServiceImpl implements KafkaService
{
   private final KafkaTemplate<String, String> kafkaTemplate;
   
   public KafkaServiceImpl(KafkaTemplate<String, String> kafkaTemplate)
   {
      this.kafkaTemplate = kafkaTemplate;
   }
   
   @Override
   public void sendMessage(String topic, String message)
   {
      kafkaTemplate.send(topic, message);
   }
}
