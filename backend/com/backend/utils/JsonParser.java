package com.backend.utils;

import java.util.Map;

import java.util.HashMap;

public class JsonParser {
    public static Map<String, Object> parseJson(String jsonString){
        Map<String, Object> jsonObject = new HashMap<>();
        jsonString = jsonString.trim();

        jsonString = jsonString.substring(1, jsonString.length() - 1).trim();

        boolean inQuotes = false;
        StringBuilder keyValueBuilder = new StringBuilder();
        for (int i = 0; i < jsonString.length(); i++) {
            char currentChar = jsonString.charAt(i);
            if (currentChar == '\"') {
                inQuotes = !inQuotes;
            }
            if (currentChar == ',' && !inQuotes) {
                processPair(jsonObject, keyValueBuilder.toString());
                keyValueBuilder.setLength(0);
            } else {
                keyValueBuilder.append(currentChar);
            }
        }
        processPair(jsonObject, keyValueBuilder.toString());

        return jsonObject;
    }

    private static void processPair(Map<String, Object> jsonObject, String pair){
        String[] keyValue = pair.split(":", 2);

        String key = keyValue[0].trim().replace("\"", "");
        String value = keyValue[1].trim();
        if (value.charAt(0) == '\"' && value.charAt(value.length() - 1) == '\"') {
            jsonObject.put(key, value.substring(1, value.length() - 1));
        } else if (value.matches("-?\\d+(\\.\\d+)?")) {
            jsonObject.put(key, Integer.parseInt(value));
        } 
    }
}
