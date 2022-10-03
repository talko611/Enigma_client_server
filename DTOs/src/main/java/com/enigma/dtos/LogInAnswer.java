package com.enigma.dtos;

import java.util.UUID;

public class LogInAnswer {
    private boolean isSuccess;
    private String message;

    private UUID id;

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
