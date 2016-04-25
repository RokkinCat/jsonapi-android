package com.jsonapi.util;

import com.jsonapi.annotations.JsonApiName;
import com.jsonapi.annotations.Relationship;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by greg on 2/12/16.
 */
public class ClassUtil {
    public static Map<String, Field> mapRelationshipFields(Class c) {
        Map<String, Field> fields = new LinkedHashMap<>();
        for(Field field : c.getDeclaredFields()) {
            if(field.isAnnotationPresent(Relationship.class)) {
                if(field.isAnnotationPresent(JsonApiName.class)) {
                    fields.put(field.getAnnotation(JsonApiName.class).value(), field);
                }else {
                    fields.put(field.getName(), field);
                }
            }
        }
        return fields;
    }
}
