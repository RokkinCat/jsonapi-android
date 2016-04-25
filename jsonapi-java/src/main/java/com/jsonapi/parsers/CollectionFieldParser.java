package com.jsonapi.parsers;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;
import com.jsonapi.util.FieldParserUtil;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by greg on 2/2/16.
 *
 * Takes a JsonArray and parses each element
 */
public class CollectionFieldParser implements FieldParser {

    @Override
    public Collection parseField(JsonValue value) {
        List<Object> retList = new LinkedList<>();
        JsonArray array = value.asArray();
        for(JsonValue v : array.values()) {
            retList.add(FieldParserUtil.getParserFor(v).parseField(v));
        }
        return retList;
    }
}
