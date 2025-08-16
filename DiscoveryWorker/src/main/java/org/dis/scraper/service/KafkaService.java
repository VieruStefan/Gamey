package org.dis.scraper.service;

public interface KafkaService
{
   void sendMessage(String topic, String message);
}
