package com.example.fooddeliverysseapp.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface EventService
{

  SseEmitter create();

  <T> void sendEvent(T payload,
      String eventName);
}
