package com.example.fooddeliverysseapp.service;

import static java.lang.Long.MAX_VALUE;
import static org.springframework.web.servlet.mvc.method.annotation.SseEmitter.event;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
class SseEventService
    implements EventService
{

  private final Collection<SseEmitter> m_emitters = new CopyOnWriteArrayList<>();

  @Override
  public SseEmitter create()
  {

    SseEmitter emitter = new SseEmitter(MAX_VALUE);

    emitter.onCompletion(() -> m_emitters.remove(emitter));
    emitter.onTimeout(() -> m_emitters.remove(emitter));
    emitter.onError(throwable -> {
      m_emitters.remove(emitter);
      emitter.completeWithError(throwable);
    });

    m_emitters.add(emitter);

    return emitter;
  }

  @Override
  public <T> void sendEvent(T payload,
      String eventName)
  {

    var event = event().data(payload).name(eventName).build();

    for (SseEmitter emitter : m_emitters)
    {
      try
      {
        emitter.send(event);
      }
      catch (IOException exception)
      {
        emitter.completeWithError(exception);
      }
    }
  }
}
