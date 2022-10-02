package com.enigma.machine.parts.plugBoard;


import java.util.Map;

public interface PlugBoard {
    String switchLetters(String letter);
    Map<String, String> getAllPlugs();
    String toString();
}