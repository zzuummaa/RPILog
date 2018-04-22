package ru.zuma.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class JSONConverter {
    public static String fromLogRecords(Map<Integer, String> records, int lastRecord) {
        StringBuffer stringBuffer = new StringBuffer();

        for (Map.Entry<Integer, String> record: records.entrySet()) {
            stringBuffer.append(record.getValue());
            if (record.getKey() > lastRecord) lastRecord = record.getKey();
        }

        ObjectMapper mapper = new ObjectMapper();

        String mapAsJson;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("log_data", stringBuffer.toString());
            map.put("last_record", lastRecord);

            mapAsJson = mapper.writeValueAsString(map);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }

        return mapAsJson;
    }

    public static String jsonToLog(String json) {
        ObjectMapper mapper = new ObjectMapper();

        String log;
        try {
            Map<String, String> map = mapper.readValue(json, new TypeReference<Map<String, String>>(){});

            log = map.get("log_data");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        return log;
    }
}
