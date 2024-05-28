package com.backend.utils;


import java.util.ArrayList;
import java.util.List;

public class StringToJsonConverter {
    public static List<Event> parseStringToEventList(String input) {
        String[] parts = input.split(", ");
        List<Event> events = new ArrayList<>();
        
        for (int i = 0; i < parts.length; i += 6) {
            Event event = new Event();
            event.setEventID(Integer.parseInt(parts[i]));
            event.setTitle(parts[i+1]);
            event.setStartedAt(parts[i+2]);
            event.setEndedAt(parts[i+3]);
            event.setDescription(parts[i+4]);
            event.setOwner(parts[i+5]);
            events.add(event);
        }
        return events;
    }

    public static String convertToJson(List<Event> events) {
        StringBuilder jsonArray = new StringBuilder();
        jsonArray.append("[");

        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            StringBuilder jsonObject = new StringBuilder();
            jsonObject.append("{");
            jsonObject.append("\"eventID\":").append(event.getEventID()).append(",");
            jsonObject.append("\"title\":\"").append(event.getTitle()).append("\",");
            jsonObject.append("\"startedAt\":\"").append(event.getStartedAt()).append("\",");
            jsonObject.append("\"endedAt\":\"").append(event.getEndedAt()).append("\",");
            jsonObject.append("\"description\":\"").append(event.getDescription()).append("\",");
            jsonObject.append("\"owner\":\"").append(event.getOwner()).append("\"");
            jsonObject.append("}");

            jsonArray.append(jsonObject);

            if (i < events.size() - 1) {
                jsonArray.append(",");
            }
        }

        jsonArray.append("]");
        return jsonArray.toString();
    }
}
