package com.enigma.utils;

import com.enigma.dtos.dataObjects.DecryptionTaskData;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.squareup.okhttp.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

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
    public static final String GET_GAME_STATUS = "get_game_status";
    public static final String GET_TASKS_RESOURCE = "get_tasks";
    public static final String REPORT_OF_CANDIDATES = "report_candidates";

    public static void main(String[] args) {
        AtomicInteger integer = new AtomicInteger(10);
        System.out.println(integer.accumulateAndGet(10, Integer::sum));
    }



}
