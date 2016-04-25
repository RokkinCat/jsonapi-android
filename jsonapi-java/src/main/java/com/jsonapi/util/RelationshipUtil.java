package com.jsonapi.util;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.jsonapi.parsers.ClassFieldParser;
import com.jsonapi.parsers.RelationshipFieldParser;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by greg on 2/12/16.
 */
public class RelationshipUtil {
    public static <T> Collection parseArray(JsonArray array, Class<T> listType, RelationshipFieldParser.IncludedLookupListener listener) {
        Collection collection = new LinkedList();
        for(JsonValue datum : array.values()) {
            String type = datum.asObject().get("type").asString();
            int id = Integer.parseInt(datum.asObject().get("id").asString());

            JsonObject relation = listener.lookup(type,id);
            if(relation != null) {
                collection.add(new RelationshipFieldParser<>(
                        listType.cast(new ClassFieldParser<>(listType).parseField(relation.get("attributes").asObject())), listener)
                        .parseField(relation.get("relationships")));
            }
        }
        return collection;
    }

    public static <T> T parseObject(JsonObject object, Class<T> c, RelationshipFieldParser.IncludedLookupListener listener) {
        String type = object.get("type").asString();
        int id = Integer.parseInt(object.get("id").asString());

        JsonObject relation = listener.lookup(type,id);
        if(relation != null) {
            return new RelationshipFieldParser<>(c.cast(new ClassFieldParser<>(c).parseField(relation.get("attributes").asObject())), listener)
                    .parseField(relation.get("relationships"));
        }else {
            return null;
        }
    }
}
