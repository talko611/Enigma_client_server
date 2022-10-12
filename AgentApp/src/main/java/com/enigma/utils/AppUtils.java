package com.enigma.utils;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import java.util.UUID;

public class AppUtils {
    public static final OkHttpClient CLIENT = new OkHttpClient();
    public static final Gson GSON_SERVICE = new Gson();
    public static UUID CLIENT_ID;
    public static final String APP_URL = "http://localhost:8080/EnigmaApp/Agent/";
    public static final String LOGIN_RESOURCE = "LogIn";
    public static final String GET_ALLIES_RESOURCE = "get_allies";



}
