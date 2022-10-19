package com.enigma.utiles;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import java.util.UUID;

public class AppUtils {
    public static UUID CLIENT_ID;
    public static final OkHttpClient CLIENT = new OkHttpClient();
    public static final Gson GSON_SERVICE = new Gson();
    public static final String APP_URL = "http://localhost:8080/EnigmaApp/allie/";
    public static final String LOGIN_RESOURCE = "login";
    public static final String GET_BATTLEFIELDS_RESOURCE = "get_battlefields";
    public static final String GET_AGENTS_RESOURCE = "get_agents";
    public static final String JOIN_BATTLEFIELD_RESOURCE = "join";
    public static final String SET_TASK_SIZE = "set_task";
    public static final String SET_READY_RESOURCE = "set_ready";
    public static final String GET_GAME_STATUS_RESOURCE = "get_game_status";
    public static final String GET_PARTICIPANTS_RESOURCE = "get_teams";

    public static void setClientId(UUID clientId) {
        CLIENT_ID = clientId;
    }
}
