package com.enigma.dtos.ClientDataTransfer;

public class EncryptMessageData {
    private String message;
    private String source;
    private String encrypted;
    private String currentMachineConfiguration;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setEncrypted(String encrypted) {
        this.encrypted = encrypted;
    }

    public String getEncrypted() {
        return encrypted;
    }

    public String getCurrentMachineConfiguration() {
        return currentMachineConfiguration;
    }

    public void setCurrentMachineConfiguration(String currentMachineConfiguration) {
        this.currentMachineConfiguration = currentMachineConfiguration;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
