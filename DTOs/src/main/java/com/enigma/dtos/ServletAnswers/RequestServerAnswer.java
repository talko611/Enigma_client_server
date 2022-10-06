package com.enigma.dtos.ServletAnswers;

public class RequestServerAnswer {
    private boolean isSuccess;
    private String message;

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }
}
