package com.jsonapi.parsers;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.jsonapi.util.FieldParserUtil;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by greg on 2/2/16.
 *
 * Takes a JsonObject object and parses each of the attributes
 */
public class MapFieldParser implements FieldParser {

    @Override
    public Map parseField(JsonValue value) {
        Map<String, Object> retMap = new LinkedHashMap<>();
        JsonObject attributes = value.asObject();

        // Navigates through each json object in the attributes array
        for(String attribute : attributes.names()) {
            JsonValue field = attributes.get(attribute);
            retMap.put(attribute, FieldParserUtil.getParserFor(field).parseField(field));
        }

        return  retMap;
    }
}
