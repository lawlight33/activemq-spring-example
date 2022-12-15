package com.example.model;

public class Order {

    private int id;

    public Order(int id) {
        this.id = id;
    }

    // Empty constructor for Json serializing
    public Order() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Order{id=" + id + "}";
    }
}
