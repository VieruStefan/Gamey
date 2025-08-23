package org.dis.diffbotapi.service;

public interface KafkaService
{
   void sendMessage(String topic, String message);
}
