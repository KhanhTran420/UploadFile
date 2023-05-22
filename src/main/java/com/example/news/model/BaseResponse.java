package com.example.news.model;

public class BaseResponse {

    private int responseStatus;
    private String responseCode;
    private String message;
    private Object data;

    public BaseResponse(int responseStatus, String responseCode, String message) {
        super();
        this.responseStatus = responseStatus;
        this.responseCode = responseCode;
        this.message = message;
    }

    public BaseResponse(String message) {
        super();
        this.message = message;
    }

    public BaseResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public BaseResponse(int responseStatus, String responseCode, String message, Object data) {
        super();
        this.responseStatus = responseStatus;
        this.responseCode = responseCode;
        this.message = message;
        this.data = data;
    }
}
