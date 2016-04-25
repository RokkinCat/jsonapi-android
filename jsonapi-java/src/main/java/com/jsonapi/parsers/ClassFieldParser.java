package com.jsonapi.parsers;

import com.eclipsesource.json.JsonValue;
import com.jsonapi.annotations.JsonApiName;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by greg on 2/1/16.
 */
public class ClassFieldParser<T> implements FieldParser<T> {

    private Class c;

    public ClassFieldParser(Class c) {
        this.c = c;
    }

    @Override
    public T parseField(JsonValue value) {
        try {
            T instance = (T) c.newInstance();
            Map<String, Object> attributeMap = new MapFieldParser().parseField(value);

            for(Field field : c.getDeclaredFields()) {
                if(field.isAnnotationPresent(JsonApiName.class) &&
                        attributeMap.containsKey(field.getAnnotation(JsonApiName.class).value())) {
                    field.setAccessible(true);
                    field.set(instance, attributeMap.get(field.getAnnotation(JsonApiName.class).value()));
                } else if(attributeMap.containsKey(field.getName())) {
                    field.setAccessible(true);
                    field.set(instance, attributeMap.get(field.getName()));
                }
            }
            return instance;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }
}
