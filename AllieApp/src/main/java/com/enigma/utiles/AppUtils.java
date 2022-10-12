package com.enigma.utiles;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import java.util.UUID;

public class AppUtils {
    public static UUID CLIENT_ID;
    public static final OkHttpClient CLIENT = new OkHttpClient();
    public static final Gson GSON_SERVICE = new Gson();
    public static final String APP_URL = "http://localhost:8080/EnigmaApp/Allie/";
    public static final String LOGIN_RESOURCE = "LogIn";
    public static final String GET_BATTLEFIELDS_RESOURCE = "Get_battlefields";
    public static final String GET_AGENTS_RESOURCE = "Get_agents";
    public static final String JOIN_BATTLEFIELD_RESOURCE = "Join";

    public static void setClientId(UUID clientId) {
        CLIENT_ID = clientId;
    }
}
