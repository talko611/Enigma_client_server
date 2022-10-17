package com.enigma.utils;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import java.util.UUID;

public class AppUtils {
    public static final OkHttpClient CLIENT = new OkHttpClient();
    public static final Gson GSON_SERVICE = new Gson();
    public static UUID CLIENT_ID;
    public static final String APP_URL = "http://localhost:8080/EnigmaApp/agent/";
    public static final String LOGIN_RESOURCE = "login";
    public static final String GET_ALLIES_RESOURCE = "get_allies";
    public static final String GET_ENIGMA_PARTS_RESOURCE = "get_enigma_parts";
    public static final String SET_READY_RESOURCE = "set_ready";
    public static final String SET_AVAILABLE_RESOURCE = "set_available";



}
