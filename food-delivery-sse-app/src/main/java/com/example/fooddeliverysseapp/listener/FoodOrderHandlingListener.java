package com.example.fooddeliverysseapp.listener;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.fooddeliverysseapp.domain.FoodOrder;
import com.example.fooddeliverysseapp.domain.observers.DeliveredObservable;
import com.example.fooddeliverysseapp.domain.observers.KitchenObservable;
import com.example.fooddeliverysseapp.domain.observers.Observable;
import com.example.fooddeliverysseapp.domain.observers.OnTheWayObservable;
import com.example.fooddeliverysseapp.domain.observers.OrderObservable;
import com.example.fooddeliverysseapp.service.EventService;

@Component
public class FoodOrderHandlingListener
{

  private final Collection<Observable> m_observables;

  public FoodOrderHandlingListener(EventService eventService)
  {
    m_observables = List.of(
                            new OrderObservable(eventService),
                            new KitchenObservable(eventService),
                            new OnTheWayObservable(eventService),
                            new DeliveredObservable(eventService));
  }

  public void notifyAll(FoodOrder orderFood)
  {
    m_observables.forEach(foodObservable -> foodObservable.update(orderFood));
  }
}
