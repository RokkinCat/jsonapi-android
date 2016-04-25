package com.jsonapi.util;

import com.eclipsesource.json.JsonValue;
import com.jsonapi.parsers.CollectionFieldParser;
import com.jsonapi.parsers.FieldParser;
import com.jsonapi.parsers.MapFieldParser;
import com.jsonapi.parsers.StringFieldParser;

/**
 * Created by greg on 2/8/16.
 */
public class FieldParserUtil {
    /**
     * Takes a JsonValue object and figures out how to parse it. i.e. if it's an array, object, null, or primative
     */
    public static FieldParser getParserFor(JsonValue value) {
        if(value.isArray()) {
            return new CollectionFieldParser();
        }else if(value.isObject()) {
            return new MapFieldParser();
        }else {
            return new StringFieldParser();
        }
    }
}
