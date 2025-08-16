package org.dis.gateway.service;

public interface KafkaService
{
   void sendMessage(String topic, String message);
}
