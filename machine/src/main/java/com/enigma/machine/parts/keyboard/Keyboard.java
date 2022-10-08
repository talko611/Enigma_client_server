package com.enigma.machine.parts.keyboard;

import java.util.List;

public interface Keyboard {
    boolean isKeyExists(String key);
    int getEntryPointByKey(String key);
    String getEntryMatchKey(int entry);
    String toString();
    int getKeyboardSize();
    List<String> getAllKeys();
}
