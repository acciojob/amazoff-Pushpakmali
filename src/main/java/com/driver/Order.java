package com.driver;

public class Order {

    private String id;
    private int deliveryTime;

    public Order(String id, String deliveryTime) {

        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
        this.id = id;
        this.deliveryTime = convertDeliveryTime(deliveryTime);
    }
    public String getId() {
        return id;
    }

    public int getDeliveryTime() {return deliveryTime;}

    private int convertDeliveryTime(String deliveryTime) {
        return TimeUtils.convertDeliveryTime(deliveryTime);
    }

    private String convertDeliveryTime(int deliveryTime){
        return TimeUtils.convertDeliveryTime(deliveryTime);
    }
}
