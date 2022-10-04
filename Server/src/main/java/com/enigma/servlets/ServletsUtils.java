package com.enigma.servlets;

import com.engine.Engine;
import com.engine.EngineImp;
import jakarta.servlet.ServletContext;

public class ServletsUtils {
    private static final String ENGINE_ATTRIBUTE_NAME = "engine";
    private static final Object engineLock = new Object();

    public static Engine getEngine(ServletContext servletContext){
        synchronized (engineLock){
            if(servletContext.getAttribute(ENGINE_ATTRIBUTE_NAME) == null){
                Engine engine = new EngineImp();
                servletContext.setAttribute(ENGINE_ATTRIBUTE_NAME, engine);
            }
        }
        return (Engine) servletContext.getAttribute(ENGINE_ATTRIBUTE_NAME);
    }
}
