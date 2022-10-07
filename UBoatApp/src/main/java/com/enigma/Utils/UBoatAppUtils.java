package com.enigma.Utils;

import com.squareup.okhttp.OkHttpClient;

import java.util.UUID;

public class UBoatAppUtils {
    public static final String APP_URL = "http://localhost:8080/EnigmaApp/UBoat/";
    public static final String LOGIN_RESOURCE = "LogIn";
    public static final String UPLOAD_FILE_RESOURCE = "load-file";
    public static final OkHttpClient HTTP_CLIENT = new OkHttpClient();
    public static UUID CLIENT_ID;

    public static void setClientId(UUID clientId) {
        CLIENT_ID = clientId;
    }
}
