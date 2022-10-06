package com.enigma.dtos.EngineAnswers;

public class InputOperationAnswer {
    private final boolean success;
    private final String message;

    public InputOperationAnswer(boolean success, String message){
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
