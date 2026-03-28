package com.example.fooddeliverysseapp.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.fooddeliverysseapp.domain.FoodOrder;
import com.example.fooddeliverysseapp.listener.FoodOrderHandlingListener;

@ExtendWith(MockitoExtension.class)
class OrderFoodServiceTest
{

  @InjectMocks
  private FoodServiceImpl foodService;

  @Mock
  private FoodOrderHandlingListener orderFoodListener;

  @Captor
  private ArgumentCaptor<FoodOrder> orderFood;

  @Test
  void shouldOrderFoodWithSuccess()
  {

    doNothing().when(orderFoodListener).notifyAll(orderFood.capture());

    assertDoesNotThrow(() -> foodService.order());
  }
}
