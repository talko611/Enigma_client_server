package com.engine;

import com.enigma.dtos.LogInAnswer;

public interface Engine {
    LogInAnswer uBoatLogIn(String name);
    LogInAnswer agentLogIn(String name);
    LogInAnswer allieLogIn(String name);
}
