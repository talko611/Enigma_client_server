package com.enigma.Utils;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import java.util.UUID;

public class AppUtils {
    public static final String APP_URL = "http://localhost:8080/EnigmaApp/uBoat/";
    public static final String LOGIN_RESOURCE = "login";
    public static final String UPLOAD_FILE_RESOURCE = "load_file";
    public static final String MACHINE_DETAILS_RESOURCE = "get_details";
    public static final String MACHINE_PARTS_DETAILS_RESOURCE = "get_machine_parts";
    public static final String CONFIGURATION_RESOURCE = "config";
    public static final String DICTIONARY_RESOURCE = "get_dictionary";
    public static final String ENCRYPT_RESOURCE = "encrypt";
    public static final String SET_READY_RESOURCE = "set_ready";
    public static final String GET_ALLIES_RESOURCE = "get_allies";
    public static final String GET_GAME_STATUS_RESOURCE = "get_game_status";
    public static final String GET_CANDIDATES_RESOURCE = "get_candidates";
    public static final OkHttpClient HTTP_CLIENT = new OkHttpClient();
    public static UUID CLIENT_ID;
    public static final Gson GSON_SERVICE = new Gson();

}
