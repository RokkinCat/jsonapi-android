package com.jsonapi.parsers;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.jsonapi.util.ClassUtil;
import com.jsonapi.util.RelationshipUtil;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Map;

/**
 * Created by greg on 2/9/16.
 */
public class RelationshipFieldParser<T> implements FieldParser<T> {

    private final T instance;
    private final Map<String, Field> fields;
    private final IncludedLookupListener listener;

    public RelationshipFieldParser(T instance, IncludedLookupListener listener) {
        this.instance = instance;
        this.listener = listener;

        this.fields = ClassUtil.mapRelationshipFields(instance.getClass());
    }

    @Override
    public T parseField(JsonValue value) {
        JsonObject relationships = value.asObject();
        // Getting all the json keys
        for(String relationshipName : relationships.names()) {
            try {
                // If the annotated fields we cached early contain this key name
                if (fields.containsKey(relationshipName)) {
                    Field field = fields.get(relationshipName);
                    field.setAccessible(true);

                    JsonValue data = relationships.get(relationshipName).asObject().get("data");
                    JsonValue links = relationships.get(relationshipName).asObject().get("links");
                    Class<?> listType;

                    // Parse data object
                    if(data != null) {
                        if (data.isArray()) {
                            listType = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                            field.set(instance, RelationshipUtil.parseArray(data.asArray(), listType, listener));
                        } else {
                            listType = field.getType();
                            field.set(instance, RelationshipUtil.parseObject(data.asObject(), listType, listener));
                        }
                    }

                    // Parse links object
                    if(links != null) new LinkParser<>(instance).parseField(links);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public interface IncludedLookupListener {
        JsonObject lookup(String type, int id);
    }
}
