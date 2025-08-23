package org.dis.gamedata.service;

public interface KafkaService
{
   void sendMessage(String topic, String message);
}
