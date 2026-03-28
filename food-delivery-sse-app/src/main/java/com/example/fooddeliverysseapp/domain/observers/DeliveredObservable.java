package com.example.fooddeliverysseapp.domain.observers;

import com.example.fooddeliverysseapp.domain.FoodOrder;
import com.example.fooddeliverysseapp.domain.FoodStatus;
import com.example.fooddeliverysseapp.service.EventService;

public class DeliveredObservable
    extends Observable
{

  public DeliveredObservable(EventService eventService)
  {
    super(eventService);
  }

  @Override
  public void update(FoodOrder orderFood)
  {

    if (orderFood.getStatus() == FoodStatus.ON_THE_WAY)
    {
      orderFood.setStatus(FoodStatus.DELIVERED);
      sendEvent(orderFood,
                "delivered");
    }
  }
}
