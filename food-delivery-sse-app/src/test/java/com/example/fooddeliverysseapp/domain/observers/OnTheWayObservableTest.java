package com.example.fooddeliverysseapp.domain.observers;

import static com.example.fooddeliverysseapp.domain.FoodStatus.IN_THE_KITCHEN;
import static com.example.fooddeliverysseapp.domain.FoodStatus.ON_THE_WAY;
import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.setDefaultTimeout;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.fooddeliverysseapp.ModelFixture;
import com.example.fooddeliverysseapp.service.EventService;

@ExtendWith(MockitoExtension.class)
class OnTheWayObservableTest
{

  @Mock
  private EventService eventService;

  @BeforeEach
  void init()
  {
    setDefaultTimeout(Duration.ofMinutes(1L));
  }

  @Test
  void shouldUpdateAnOnTheWayObservable()
  {

    var observer = new OnTheWayObservable(eventService);
    var food = ModelFixture.buildFood();
    food.setStatus(IN_THE_KITCHEN);

    Mockito.doNothing().when(eventService).sendEvent(food,
                                                     "on-the-way");

    observer.update(food);

    await().untilAsserted(() -> assertEquals(ON_THE_WAY,
                                             food.getStatus()));
  }
}
