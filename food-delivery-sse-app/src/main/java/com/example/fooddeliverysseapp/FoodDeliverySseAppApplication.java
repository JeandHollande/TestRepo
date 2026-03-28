package com.example.fooddeliverysseapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Architectural flow:
 * <ol>
 * <li>FoodOrderHandlingListener is instantiated, containing the list of 4 new constructed observables (OrderObservable,
 * DeliveredObservable etc.)
 * <li>FoodServiceImpl is instantiated, containing the FoodOrderHandlingListener
 * <li>SseService is created containing an empty list of SseEmitters (standard Java class)
 * <li>When index.html is loaded, the Java script block constructs an EventSource with a mapping of "/order-status", and
 * adds 4 listeners which listen to events which are defined in the 4 Observables, if called, will call the JavaScript
 * render method
 * <li>The creation of the "/order-status" mapping will call the related @GetMapping in the OrderFoodController, which
 * will call the EventService.create method
 * <li>This will create add a new SseEmitter to the emitters list in the SseService, and will configure the emitter with
 * what to do in several meta events.
 * <li>If in index.html the "Create Order" butting is pressed, this calls the OrderFoodController "/order-food" mapping
 * which calls FoodServiceImpl.order
 * <li>A new FoodOrder is constructed, with a random generated id and a status of FoodStatus.ORDER_PLACED
 * <li>FoodServiceImpl.order calls FoodOrderHandlingListener.notifyAll
 * <li>notifyAll calls update to each of the 4 Observables
 * <li>The implemented update methods form some kind of state machine using the FoodStatus enum.<br>
 * When the respective state is set, the new state is set, then the EventService.sendEvent is called with the FoodOrder
 * and a String containing the event name (as defined in the javascript EventSource.addEventListener calls). At last the
 * waitForProcess method is called which makes the process sleep for a while.
 * <li>sendEvent will call EventService.sendEvent which will create en SseEvent, en send the event to all emitters in
 * the emitter list (only 1 in this case).
 * <li>The event is received in the JavaScript EventSource listener, and rendered by adding a new element to the div
 * with id "order-status"
 * <li>The content is created by calling the getOrderStatus(event) method, which parses the JSON content of the
 * event.data element, and fetches the order.id and the order.status.
 */
@SpringBootApplication
public class FoodDeliverySseAppApplication
{

  public static void main(String[] args)
  {
    SpringApplication.run(FoodDeliverySseAppApplication.class,
                          args);
  }

}
