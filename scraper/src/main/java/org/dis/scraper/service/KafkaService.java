package org.dis.scraper.service;

public interface KafkaService
{
   public void sendMessage(String topic, String message);
}
