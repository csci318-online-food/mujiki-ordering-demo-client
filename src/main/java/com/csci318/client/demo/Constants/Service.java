package com.csci318.client.demo.Constants;

public enum Service {
    AUTHENTICATION(8080, "auth"),
    USER(8081, "user"),
    RESTAURANT(8082, "restaurant"),
    ITEM(8082, "item"),
    PROMOTION(8082, "promotion"),
    CART(8083, "cart"),
    FEEDBACK(8084, "feedback"),
    ADDRESS(8086, "address"),
    PAYMENT(8087, "payment"),
    ORDER(8088, "order"),
    ANALYTICS(8089, "analytics");

    public int Port;
    public String Base;

    Service(int port, String base) {
        this.Port = port;
        this.Base = base;
    }
}
