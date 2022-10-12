package com.enigma.dtos.EngineAnswers;

public class CalculationOperationAnswer <T>{
    private T data;
    private boolean isSuccess;
    private String message;

    public T getData() {
        return data;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
