package com.example.fooddeliverysseapp.domain;

import java.util.Random;

import static com.example.fooddeliverysseapp.domain.FoodStatus.ORDER_PLACED;

public class FoodOrder {

    private final Integer m_id;

    private FoodStatus m_status;

    public FoodOrder() {
        this.m_id = new Random().nextInt(3000, 5000);
        this.m_status = ORDER_PLACED;
    }

    public Integer getId() {
        return m_id;
    }

    public FoodStatus getStatus() {
        return m_status;
    }

    public void setStatus(FoodStatus status) {
        this.m_status = status;
    }
}
