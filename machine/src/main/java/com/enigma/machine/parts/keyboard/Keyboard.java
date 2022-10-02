package com.enigma.machine.parts.keyboard;

public interface Keyboard {
    boolean isKeyExists(String key);
    Integer getEntryPointByKey(String key);
    String getEntryMatchKey(int entry);
    String toString();
}
