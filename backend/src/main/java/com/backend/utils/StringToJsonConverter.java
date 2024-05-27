package com.backend.utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    public static String convertToJson(List<Event> events) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(events);
    }
}
