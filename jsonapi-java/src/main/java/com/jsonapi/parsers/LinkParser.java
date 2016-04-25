package com.jsonapi.parsers;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.jsonapi.annotations.Link;

import java.lang.reflect.Field;

/**
 * Created by greg on 4/25/16.
 */
public class LinkParser<T> implements FieldParser<T>{

    private final T instance;
    private final Class<T> tClass;

    public LinkParser(T instance) {
        this.instance = instance;
        this.tClass = (Class<T>) instance.getClass();
    }

    @Override
    public T parseField(JsonValue value) {
        JsonObject links = value.asObject();
        for(Field field : instance.getClass().getDeclaredFields()) {
            try {
                if (field.isAnnotationPresent(Link.class)) {
                    JsonValue link = links.get(field.getAnnotation(Link.class).value());
                    if (link != null && link.isString()) {
                        field.setAccessible(true);
                        field.set(instance, link.asString());
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }
}
