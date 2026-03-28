package com.example.fooddeliverysseapp.service;

import org.springframework.stereotype.Component;

import com.example.fooddeliverysseapp.domain.FoodOrder;
import com.example.fooddeliverysseapp.listener.FoodOrderHandlingListener;

@Component
class FoodServiceImpl
    implements FoodService
{

  private final FoodOrderHandlingListener m_orderFoodListener;

  FoodServiceImpl(FoodOrderHandlingListener orderFoodListener)
  {
    m_orderFoodListener = orderFoodListener;
  }

  @Override
  public void order()
  {
    m_orderFoodListener.notifyAll(new FoodOrder());
  }
}
