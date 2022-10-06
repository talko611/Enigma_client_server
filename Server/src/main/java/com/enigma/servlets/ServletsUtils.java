package com.enigma.servlets;

import com.engine.Engine;
import com.engine.EngineImp;
import com.engine.users.UserManager;
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
}
