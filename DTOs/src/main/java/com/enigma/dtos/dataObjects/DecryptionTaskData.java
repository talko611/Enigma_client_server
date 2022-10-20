package com.enigma.dtos.dataObjects;

import java.util.List;

public class DecryptionTaskData {
    private List<Integer> rotorsId;
    private List<Integer> offsets;
    private int reflectorId;
    private long taskSize;

    public List<Integer> getRotorsId() {
        return rotorsId;
    }

    public List<Integer> getOffsets() {
        return offsets;
    }

    public int getReflectorId() {
        return reflectorId;
    }

    public long getTaskSize() {
        return taskSize;
    }

    public void setRotorsId(List<Integer> rotorsId) {
        this.rotorsId = rotorsId;
    }

    public void setOffsets(List<Integer> offsets) {
        this.offsets = offsets;
    }

    public void setReflectorId(int reflectorId) {
        this.reflectorId = reflectorId;
    }

    public void setTaskSize(long taskSize) {
        this.taskSize = taskSize;
    }
}
