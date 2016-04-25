package com.jsonapi.test;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import com.jsonapi.parsers.ClassFieldParser;
import com.jsonapi.parsers.CollectionFieldParser;
import com.jsonapi.parsers.MapFieldParser;
import com.jsonapi.parsers.StringFieldParser;
import com.jsonapi.util.FieldParserUtil;
import org.junit.Test;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by greg on 2/11/16.
 */
public class FieldParserTest {

    private static final String JSON_OBJECT = "{\"links\": {\"self\": \"http://example.com/articles\", \"next\": \"http://example.com/articles?page[offset]=2\",\"last\": \"http://example.com/articles?page[offset]=10\"}}";
    private static final String JSON_ARRAY = "[{ \"type\": \"comments\", \"id\": \"5\" },{ \"type\": \"comments\", \"id\": \"12\" }]";
    private static final String JSON_VALUE = "{\"type\": \"comments\"}";
    private static final String JSON_VALUE_NULL = "{\"type\": null}";
    private static final String JSON_AUTHOR = "{\"name\":\"J. R. R. Tolkien\",\"date_of_birth\":\"1892-01-03\",\"date_of_death\":\"1973-09-02\",\"created_at\":\"2016-02-08 15:57:00\",\"updated_at\":\"2016-02-08 15:57:00\"}";

    @Test
    public void testFieldParserUtilObject() {
        JsonValue object = Json.parse(JSON_OBJECT);
        assertTrue(FieldParserUtil.getParserFor(object) instanceof MapFieldParser);
    }

    @Test
    public void testFieldParserUtilArray() {
        JsonValue array = Json.parse(JSON_ARRAY);
        assertTrue(FieldParserUtil.getParserFor(array) instanceof CollectionFieldParser);
    }

    @Test
    public void testFieldParserUtilValue() {
        JsonObject value = Json.parse(JSON_VALUE).asObject();
        assertTrue(FieldParserUtil.getParserFor(value.get("type")) instanceof StringFieldParser);
    }

    @Test
    public void testStringFieldParserWithString() {
        JsonObject value = Json.parse(JSON_VALUE).asObject();
        assertTrue(FieldParserUtil.getParserFor(value.get("type")) instanceof StringFieldParser);
        assertTrue(new StringFieldParser().parseField(value.get("type")).equals("comments"));
    }

    @Test
    public void testStringFieldParserWithNull() {
        JsonObject value = Json.parse(JSON_VALUE_NULL).asObject();
        assertTrue(FieldParserUtil.getParserFor(value.get("type")) instanceof StringFieldParser);
        assertTrue(new StringFieldParser().parseField(value.get("type")).equals("null"));
    }

    @Test
    public void testCollectionFieldParserWithValid() {
        JsonValue array = Json.parse(JSON_ARRAY);
        assertTrue(FieldParserUtil.getParserFor(array) instanceof CollectionFieldParser);
        CollectionFieldParser parser = new CollectionFieldParser();
        Collection<Map<String, Object>> list = parser.parseField(array);

        assertNotNull(list);

        Iterator<Map<String, Object>> iter = list.iterator();

        assertTrue(list.size() == 2);
        assertTrue(iter.next().get("id").equals("5"));
        assertTrue(iter.next().get("id").equals("12"));
    }

    @Test
    public void testCollectionFieldParserWithInvalid() {
        JsonValue object = Json.parse(JSON_OBJECT);
        assertTrue(FieldParserUtil.getParserFor(object) instanceof MapFieldParser);
        CollectionFieldParser parser = new CollectionFieldParser();

        try {
            parser.parseField(object);
            fail();
        }catch(UnsupportedOperationException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testMapFieldParserWithValid() {
        JsonValue value = Json.parse(JSON_VALUE);
        assertTrue(FieldParserUtil.getParserFor(value) instanceof MapFieldParser);
        MapFieldParser parser = new MapFieldParser();

        Map<String, String> map = parser.parseField(value);
        assertNotNull(map);
        assertTrue(map.get("type").equals("comments"));
    }

    @Test
    public void testMapFieldParserWithNestedMaps() {
        JsonValue object = Json.parse(JSON_OBJECT);
        assertTrue(FieldParserUtil.getParserFor(object) instanceof MapFieldParser);
        MapFieldParser parser = new MapFieldParser();

        Map<String, Map<String, Object>> map = parser.parseField(object);
        assertNotNull(map);
        assertTrue(map.get("links") instanceof Map);
        assertTrue(map.get("links").get("self") instanceof String);
        assertTrue(map.get("links").get("self").equals("http://example.com/articles"));
    }

    @Test
    public void testMapFieldParserWithInvalid() {
        JsonValue array = Json.parse(JSON_ARRAY);
        assertTrue(FieldParserUtil.getParserFor(array) instanceof CollectionFieldParser);
        MapFieldParser parser = new MapFieldParser();

        try {
            parser.parseField(array);
            fail();
        }catch(UnsupportedOperationException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testClassFieldParserWithValidAuthor() {
        JsonValue object = Json.parse(JSON_AUTHOR);
        ClassFieldParser<Author> parser = new ClassFieldParser<>(Author.class);
        Author author = parser.parseField(object);

        assertNotNull(author);
        assertTrue(author.name.equals("J. R. R. Tolkien"));
        assertTrue(author.dob.equals("1892-01-03"));
        assertTrue(author.dod.equals("1973-09-02"));
        assertTrue(author.created_at.equals("2016-02-08 15:57:00"));
    }

    @Test
    public void testClassFieldParserWithInvalidAuthor() {
        JsonValue object = Json.parse(JSON_OBJECT);
        ClassFieldParser<Author> parser = new ClassFieldParser<>(Author.class);
        Author author = parser.parseField(object);

        assertNotNull(author);
        assertNull(author.name);
        assertNull(author.dob);
        assertNull(author.dod);
        assertNull(author.created_at);
    }
}
