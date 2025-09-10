package com.RESTfull_API_CRUD_Example.dto;

public class Serdar {
    private String status;
    
    public Serdar(String status) {
        this.status = status;
    }
    public Serdar() {             
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    
}