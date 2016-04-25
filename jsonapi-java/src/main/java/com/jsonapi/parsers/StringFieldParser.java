package com.jsonapi.parsers;

import com.eclipsesource.json.JsonValue;
import com.jsonapi.parsers.FieldParser;

/**
 * Created by greg on 2/1/16.
 */
public class StringFieldParser implements FieldParser<String> {
    @Override
    public String parseField(JsonValue value) {
        return value.isNull() ? "null" : value.asString();
    }
}
