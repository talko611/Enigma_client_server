package com.enigma.machine;

import com.enigma.machine.parts.keyboard.Keyboard;
import com.enigma.machine.parts.plugBoard.PlugBoard;
import com.enigma.machine.parts.reflector.Reflector;
import com.enigma.machine.parts.rotor.Rotor;
import java.util.List;

public interface Machine {
    void setRotors(List<Rotor> rotors);
    void setReflector(Reflector reflector);
    void setPlugBord(PlugBoard plugBord);
    void setKeyboard(Keyboard keyboard);
    String encryptDecrypt(String input);
    void reset();
    boolean isKeyExists(String key);
}
