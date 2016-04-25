package com.jsonapi;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.jsonapi.parsers.ClassFieldParser;
import com.jsonapi.parsers.LinkParser;
import com.jsonapi.parsers.RelationshipFieldParser;
import com.jsonapi.util.IncludedUtil;

import java.util.*;

/**
 * Created by greg on 2/8/16.
 */
public class JsonApiParser implements RelationshipFieldParser.IncludedLookupListener{

    private Map<String, Map<Integer, JsonObject>> includedMap;

    public JsonApiParser() {
        includedMap = new LinkedHashMap<>();
    }

    public <T> Object parse(String json, Class<T> clazz) {
        return parseJson(json, clazz);
    }

    private <T> Object parseJson(String json, Class<T> clazz) {
        // Root jsonapi object
        JsonObject root = Json.parse(json).asObject();

        // top-level member: included
        // an array of resource objects that are related to the primary data and/or each other
        JsonValue includedValue = root.get("included");
        if(includedValue != null) {
            JsonArray included = root.get("included").asArray();
            includedMap = IncludedUtil.parseIncluded(included);
        }

        // top-level member: data
        // the document's "primary data"
        JsonValue dataValue = root.get("data");
        if(dataValue.isArray()) {
            return parseDataArray(dataValue.asArray(), clazz);
        }else {
            return parseDatum(dataValue.asObject(), clazz);
        }
    }

    private <T> List<T> parseDataArray(JsonArray data, Class<T> clazz) {
        List<T> retVal = new LinkedList<>();

        // Iterates through each resource object of the primary data
        for(JsonValue value : data.values()) {
            T val = parseDatum(value.asObject(), clazz);
            if(val != null) retVal.add(val);
        }

        return retVal;
    }

    private <T> T parseDatum(JsonObject datum, Class<T> clazz) {
        // top-level member: id
        String id = datum.get("id").asString();

        // top-level member: type
        String type = datum.get("type").asString();

        // optional top-level member: attributes
        // an attributes object representing some of the resource's data.
        JsonObject attributes = datum.get("attributes").asObject();

        // optional top-level member: relationships
        // a relationships object describing relationships between the resource and other JSON API resources.
        JsonObject relationships = datum.get("relationships").asObject();

        // optional top-level member: links
        // a links object containing links related to the resource.
        JsonObject links = datum.get("links").asObject();

        return clazz.cast(
            new LinkParser<>(
                new RelationshipFieldParser<>(
                    new ClassFieldParser(clazz).parseField(attributes), this
                ).parseField(relationships)
            ).parseField(links)
        );
    }

    @Override
    public JsonObject lookup(String type, int id) {
        if(includedMap != null && includedMap.containsKey(type) && includedMap.get(type).containsKey(id)) {
            return includedMap.get(type).get(id);
        }else {
            return null;
        }
    }
}
