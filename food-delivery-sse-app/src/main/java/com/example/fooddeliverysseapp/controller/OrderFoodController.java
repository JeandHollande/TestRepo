package com.example.fooddeliverysseapp.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.fooddeliverysseapp.service.EventService;
import com.example.fooddeliverysseapp.service.FoodService;

@RestController
class OrderFoodController
{

  private final EventService m_eventService;

  private final FoodService m_foodService;

  OrderFoodController(EventService eventService,
      FoodService foodService)
  {
    m_eventService = eventService;
    m_foodService = foodService;
  }

  @GetMapping(path = "/order-status", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  SseEmitter orderStatus()
  {
    return m_eventService.create();
  }

  @PostMapping(path = "/order-food", produces = MediaType.APPLICATION_JSON_VALUE)
  void orderFood()
  {
    m_foodService.order();
  }
}
