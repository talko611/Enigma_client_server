package com.enigma.Utils;

import com.squareup.okhttp.OkHttpClient;

import java.util.UUID;

public class UBoatAppUtils {
    public static final String APP_URL = "http://localhost:8080/EnigmaApp/UBoat/";
    public static final String LOGIN_RESOURCE = "LogIn";
    public static final String UPLOAD_FILE_RESOURCE = "load-file";
    public static final String MACHINE_DETAILS_RESOURCE = "getDetails";
    public static final String MACHINE_PARTS_DETAILS_RESOURCE = "getMachineParts";
    public static final String CONFIGURATION_RESOURCE = "Config";
    public static final String DICTIONARY_RESOURCE = "getDictionary";
    public static final String ENCRYPT_RESOURCE = "encrypt";
    public static final OkHttpClient HTTP_CLIENT = new OkHttpClient();
    public static UUID CLIENT_ID;

    public static void setClientId(UUID clientId) {
        CLIENT_ID = clientId;
    }
}
