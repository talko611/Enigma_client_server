package com.engine;

import com.enigma.dtos.LoadFileAnswer;
import com.enigma.dtos.LogInAnswer;

import java.io.InputStream;
import java.util.UUID;

public interface Engine {
    LogInAnswer uBoatLogIn(String name);
    LogInAnswer agentLogIn(String name);
    LogInAnswer allieLogIn(String name);
    LoadFileAnswer loadGame(UUID uBoatId, InputStream fileData);
}
