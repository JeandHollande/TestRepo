package com.example.fooddeliverysseapp.domain.observers;

import com.example.fooddeliverysseapp.domain.FoodOrder;
import com.example.fooddeliverysseapp.service.EventService;

public abstract class Observable
{

  private final EventService m_eventService;

  protected Observable(EventService eventService)
  {
    m_eventService = eventService;
  }

  protected void waitForProcess()
  {

    try
    {
      Thread.sleep(2000L);
    }
    catch (InterruptedException e)
    {
      throw new RuntimeException(e);
    }
  }

  protected void sendEvent(FoodOrder orderFood,
      String eventName)
  {
    m_eventService.sendEvent(orderFood,
                             eventName);
  }

  public abstract void update(FoodOrder orderFood);
}
