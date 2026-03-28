package com.example.fooddeliverysseapp.listener;

import static com.example.fooddeliverysseapp.domain.FoodStatus.DELIVERED;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.fooddeliverysseapp.ModelFixture;
import com.example.fooddeliverysseapp.listener.FoodOrderHandlingListener;
import com.example.fooddeliverysseapp.service.EventService;

@ExtendWith(MockitoExtension.class)
class OrderFoodListenerTest
{

  @InjectMocks
  private FoodOrderHandlingListener orderFoodListener;

  @Mock
  private EventService eventService;

  @BeforeEach
  void init()
  {
    Awaitility.setDefaultTimeout(Duration.ofMinutes(1L));
  }

  @Test
  void shouldNotifyAllObservables()
  {

    var food = ModelFixture.buildFood();

    orderFoodListener.notifyAll(food);

    Awaitility.await().untilAsserted(() -> assertEquals(DELIVERED,
                                                        food.getStatus()));
  }
}
