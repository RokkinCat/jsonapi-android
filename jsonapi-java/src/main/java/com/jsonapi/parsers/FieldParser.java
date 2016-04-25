package com.jsonapi.parsers;

import com.eclipsesource.json.JsonValue;

import java.lang.reflect.Field;

/**
 * Created by greg on 2/1/16.
 */
public interface FieldParser<T> {
    public T parseField(JsonValue value);
}
