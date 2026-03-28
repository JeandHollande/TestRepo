package com.example.fooddeliverysseapp.domain.observers;

import static com.example.fooddeliverysseapp.domain.FoodStatus.IN_THE_KITCHEN;
import static com.example.fooddeliverysseapp.domain.FoodStatus.ON_THE_WAY;

import com.example.fooddeliverysseapp.domain.FoodOrder;
import com.example.fooddeliverysseapp.service.EventService;

public class OnTheWayObservable
    extends Observable
{

  public OnTheWayObservable(EventService eventService)
  {
    super(eventService);
  }

  @Override
  public void update(FoodOrder orderFood)
  {

    if (orderFood.getStatus() == IN_THE_KITCHEN)
    {
      orderFood.setStatus(ON_THE_WAY);
      sendEvent(orderFood,
                "on-the-way");
      waitForProcess();
    }
  }
}
