package com.example.fooddeliverysseapp;

import com.example.fooddeliverysseapp.domain.FoodOrder;

public class ModelFixture {

    private ModelFixture() {}

    public static FoodOrder buildFood() {

        return new FoodOrder();
    }
}
