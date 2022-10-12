package com.enigma.utiles;

import com.squareup.okhttp.OkHttpClient;

import java.util.UUID;

public class AppUtils {
    public static UUID CLIENT_ID;
    public static final OkHttpClient CLIENT = new OkHttpClient();
    public static final String APP_URL = "http://localhost:8080/EnigmaApp/Allie/";
    public static final String LOGIN_RESOURCE = "LogIn";
    public static final String GET_BATTLEFIELDS_RESOURCE = "Get_battlefields";

    public static void setClientId(UUID clientId) {
        CLIENT_ID = clientId;
    }
}
