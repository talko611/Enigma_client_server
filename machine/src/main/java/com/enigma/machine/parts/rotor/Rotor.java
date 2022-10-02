package com.enigma.machine.parts.rotor;

import com.enigma.machine.enums.RotorDirection;

public interface Rotor {
    int scramble(int entryPoint, RotorDirection direction);
    boolean move(boolean canMove);
    String getCurrentOffset();
    int getNotchStepsToZero();
    void reset();
    void setOffset(String letter);
    String toString();
}
