package org.dis.worker.detail.service;

public interface KafkaService
{
   public void sendMessage(String topic, String message);
}
