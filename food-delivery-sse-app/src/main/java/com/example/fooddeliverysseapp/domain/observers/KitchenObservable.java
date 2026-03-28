package com.example.fooddeliverysseapp.domain.observers;

import com.example.fooddeliverysseapp.domain.FoodOrder;
import com.example.fooddeliverysseapp.domain.FoodStatus;
import com.example.fooddeliverysseapp.service.EventService;

public class KitchenObservable
    extends Observable
{

  public KitchenObservable(EventService eventService)
  {
    super(eventService);
  }

  @Override
  public void update(FoodOrder orderFood)
  {

    if (orderFood.getStatus() == FoodStatus.ORDER_PLACED)
    {
      orderFood.setStatus(FoodStatus.IN_THE_KITCHEN);
      sendEvent(orderFood,
                "kitchen");
      waitForProcess();
    }
  }
}
