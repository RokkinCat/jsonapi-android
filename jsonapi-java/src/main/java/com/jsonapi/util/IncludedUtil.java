package com.jsonapi.util;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by greg on 2/18/16.
 */
public class IncludedUtil {

    public static Map<String, Map<Integer, JsonObject>> parseIncluded(JsonArray included) {
        Map<String, Map<Integer, JsonObject>> includedMap = new LinkedHashMap<>();

        for(JsonValue value: included.values()) {
            JsonObject item = value.asObject();
            int id = Integer.parseInt(item.get("id").asString());

            // top-level member: type
            String type = item.get("type").asString();

            // optional top-level member: attributes
            // an attributes object representing some of the resources data
            JsonValue attributes = item.get("attributes");

            // attributes is an optional
            if(attributes != null) {
                // categorizes the resource object based on id and type
                if (includedMap.containsKey(type)) {
                    includedMap.get(type).put(id, item);
                } else {
                    Map<Integer, JsonObject> typeMap = new HashMap<>();
                    typeMap.put(id, item);
                    includedMap.put(type, typeMap);
                }
            }
        }

        return includedMap;
    }
}
