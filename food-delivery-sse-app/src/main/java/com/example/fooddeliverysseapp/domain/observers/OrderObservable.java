package com.example.fooddeliverysseapp.domain.observers;

import static com.example.fooddeliverysseapp.domain.FoodStatus.ORDER_PLACED;

import com.example.fooddeliverysseapp.domain.FoodOrder;
import com.example.fooddeliverysseapp.service.EventService;

public class OrderObservable
    extends Observable
{

  public OrderObservable(EventService eventService)
  {
    super(eventService);
  }

  @Override
  public void update(FoodOrder orderFood)
  {

    if (orderFood.getStatus() == ORDER_PLACED)
    {
      sendEvent(orderFood,
                "order");
    }
  }
}
