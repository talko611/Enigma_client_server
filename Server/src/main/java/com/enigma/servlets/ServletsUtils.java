package com.enigma.servlets;

import com.engine.Engine;
import com.engine.EngineImp;
import com.engine.users.UserManager;
import com.engine.users.battlefield.Battlefield;
import com.enigma.dtos.dataObjects.GameDetailsObject;
import jakarta.servlet.ServletContext;

public class ServletsUtils {
    private static final String ENGINE_ATTRIBUTE_NAME = "engine";
    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
    private static final Object engineLock = new Object();
    private static final Object usersLock = new Object();

    public static Engine getEngine(ServletContext servletContext){
        synchronized (engineLock){
            if(servletContext.getAttribute(ENGINE_ATTRIBUTE_NAME) == null){
                Engine engine = new EngineImp();
                servletContext.setAttribute(ENGINE_ATTRIBUTE_NAME, engine);
            }
        }
        return (Engine) servletContext.getAttribute(ENGINE_ATTRIBUTE_NAME);
    }

    public static UserManager getUserManager(ServletContext servletContext){
        synchronized (usersLock){
            if(servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null){
                UserManager userManager = new UserManager();
                servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, userManager);
            }
        }
        return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }
    public static GameDetailsObject getGameStatus(Battlefield battlefield, UserManager userManager){
        GameDetailsObject gameStatus = new GameDetailsObject();
        gameStatus.setBattlefieldName(battlefield.getName());
        gameStatus.setuBoatName(userManager.getUBoatById(battlefield.getUBoatId()).getName());
        gameStatus.setParticipantsStatus(battlefield.getTeams().size() + "/" + battlefield.getEnigmaParts().getBattlefieldParts().getNumOfAllies());
        gameStatus.setGameStatus(battlefield.getGameStatus());
        gameStatus.setDecryptionLevel(battlefield.getEnigmaParts().getBattlefieldParts().getDifficulty().toString());
        gameStatus.setEncryptedMessage(battlefield.getEncryptedMessage());
        return gameStatus;
    }
}
