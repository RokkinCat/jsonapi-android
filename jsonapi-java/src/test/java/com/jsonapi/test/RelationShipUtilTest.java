package com.jsonapi.test;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.jsonapi.parsers.RelationshipFieldParser;
import com.jsonapi.util.IncludedUtil;
import com.jsonapi.util.RelationshipUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.util.*;

/**
 * Created by greg on 2/12/16.
 */
public class RelationShipUtilTest {
    private static final String JSONAPI_RELATIONSHIP_DATA_ARRAY_BOOKS = "[{\"id\":\"1\",\"type\":\"books\"},{\"id\":\"2\",\"type\":\"books\"},{\"id\":\"3\",\"type\":\"books\"},{\"id\":\"11\",\"type\":\"books\"}]";
    private static final String JSONAPI_RELATIONSHIP_DATA_OBJECT_BOOK = "{\"id\":\"1\",\"type\":\"books\"}";
    private static final String JSONAPI_INCLUDED_ARRAY = "[{\"id\":\"1\",\"type\":\"books\",\"attributes\":{\"date_published\":\"1954-07-29\",\"title\":\"The Fellowship of the Ring\",\"created_at\":\"2016-02-12 17:50:37\",\"updated_at\":\"2016-02-12 17:50:37\"},\"relationships\":{\"chapters\":{\"links\":{\"self\":\"/v1/authors/1/relationships/chapters\",\"related\":\"/v1/authors/1/chapters\"}},\"firstChapter\":{\"links\":{\"self\":\"/v1/authors/1/relationships/firstChapter\",\"related\":\"/v1/authors/1/firstChapter\"}},\"series\":{\"links\":{\"self\":\"/v1/authors/1/relationships/series\",\"related\":\"/v1/authors/1/series\"},\"data\":{\"id\":\"1\",\"type\":\"series\"}},\"author\":{\"links\":{\"self\":\"/v1/authors/1/relationships/author\",\"related\":\"/v1/authors/1/author\"},\"data\":{\"id\":\"1\",\"type\":\"authors\"}},\"stores\":{\"links\":{\"self\":\"/v1/authors/1/relationships/stores\",\"related\":\"/v1/authors/1/stores\"}},\"photos\":{\"links\":{\"self\":\"/v1/authors/1/relationships/photos\",\"related\":\"/v1/authors/1/photos\"}}},\"links\":{\"self\":\"/v1/authors/1\"}},{\"id\":\"2\",\"type\":\"books\",\"attributes\":{\"date_published\":\"1954-11-11\",\"title\":\"The Two Towers\",\"created_at\":\"2016-02-12 17:50:37\",\"updated_at\":\"2016-02-12 17:50:37\"},\"relationships\":{\"chapters\":{\"links\":{\"self\":\"/v1/authors/2/relationships/chapters\",\"related\":\"/v1/authors/2/chapters\"}},\"firstChapter\":{\"links\":{\"self\":\"/v1/authors/2/relationships/firstChapter\",\"related\":\"/v1/authors/2/firstChapter\"}},\"series\":{\"links\":{\"self\":\"/v1/authors/2/relationships/series\",\"related\":\"/v1/authors/2/series\"},\"data\":{\"id\":\"1\",\"type\":\"series\"}},\"author\":{\"links\":{\"self\":\"/v1/authors/2/relationships/author\",\"related\":\"/v1/authors/2/author\"},\"data\":{\"id\":\"1\",\"type\":\"authors\"}},\"stores\":{\"links\":{\"self\":\"/v1/authors/2/relationships/stores\",\"related\":\"/v1/authors/2/stores\"}},\"photos\":{\"links\":{\"self\":\"/v1/authors/2/relationships/photos\",\"related\":\"/v1/authors/2/photos\"}}},\"links\":{\"self\":\"/v1/authors/2\"}},{\"id\":\"3\",\"type\":\"books\",\"attributes\":{\"date_published\":\"1955-10-20\",\"title\":\"Return of the King\",\"created_at\":\"2016-02-12 17:50:37\",\"updated_at\":\"2016-02-12 17:50:37\"},\"relationships\":{\"chapters\":{\"links\":{\"self\":\"/v1/authors/3/relationships/chapters\",\"related\":\"/v1/authors/3/chapters\"}},\"firstChapter\":{\"links\":{\"self\":\"/v1/authors/3/relationships/firstChapter\",\"related\":\"/v1/authors/3/firstChapter\"}},\"series\":{\"links\":{\"self\":\"/v1/authors/3/relationships/series\",\"related\":\"/v1/authors/3/series\"},\"data\":{\"id\":\"1\",\"type\":\"series\"}},\"author\":{\"links\":{\"self\":\"/v1/authors/3/relationships/author\",\"related\":\"/v1/authors/3/author\"},\"data\":{\"id\":\"1\",\"type\":\"authors\"}},\"stores\":{\"links\":{\"self\":\"/v1/authors/3/relationships/stores\",\"related\":\"/v1/authors/3/stores\"}},\"photos\":{\"links\":{\"self\":\"/v1/authors/3/relationships/photos\",\"related\":\"/v1/authors/3/photos\"}}},\"links\":{\"self\":\"/v1/authors/3\"}},{\"id\":\"11\",\"type\":\"books\",\"attributes\":{\"date_published\":\"1937-09-21\",\"title\":\"The Hobbit\",\"created_at\":\"2016-02-12 17:50:37\",\"updated_at\":\"2016-02-12 17:50:37\"},\"relationships\":{\"chapters\":{\"links\":{\"self\":\"/v1/authors/11/relationships/chapters\",\"related\":\"/v1/authors/11/chapters\"}},\"firstChapter\":{\"links\":{\"self\":\"/v1/authors/11/relationships/firstChapter\",\"related\":\"/v1/authors/11/firstChapter\"}},\"series\":{\"links\":{\"self\":\"/v1/authors/11/relationships/series\",\"related\":\"/v1/authors/11/series\"},\"data\":null},\"author\":{\"links\":{\"self\":\"/v1/authors/11/relationships/author\",\"related\":\"/v1/authors/11/author\"},\"data\":{\"id\":\"1\",\"type\":\"authors\"}},\"stores\":{\"links\":{\"self\":\"/v1/authors/11/relationships/stores\",\"related\":\"/v1/authors/11/stores\"}},\"photos\":{\"links\":{\"self\":\"/v1/authors/11/relationships/photos\",\"related\":\"/v1/authors/11/photos\"}}},\"links\":{\"self\":\"/v1/authors/11\"}},{\"id\":\"4\",\"type\":\"books\",\"attributes\":{\"date_published\":\"1997-06-26\",\"title\":\"Harry Potter and the Philosopher's Stone\",\"created_at\":\"2016-02-12 17:50:37\",\"updated_at\":\"2016-02-12 17:50:37\"},\"relationships\":{\"chapters\":{\"links\":{\"self\":\"/v1/authors/4/relationships/chapters\",\"related\":\"/v1/authors/4/chapters\"}},\"firstChapter\":{\"links\":{\"self\":\"/v1/authors/4/relationships/firstChapter\",\"related\":\"/v1/authors/4/firstChapter\"}},\"series\":{\"links\":{\"self\":\"/v1/authors/4/relationships/series\",\"related\":\"/v1/authors/4/series\"},\"data\":{\"id\":\"2\",\"type\":\"series\"}},\"author\":{\"links\":{\"self\":\"/v1/authors/4/relationships/author\",\"related\":\"/v1/authors/4/author\"},\"data\":{\"id\":\"2\",\"type\":\"authors\"}},\"stores\":{\"links\":{\"self\":\"/v1/authors/4/relationships/stores\",\"related\":\"/v1/authors/4/stores\"}},\"photos\":{\"links\":{\"self\":\"/v1/authors/4/relationships/photos\",\"related\":\"/v1/authors/4/photos\"}}},\"links\":{\"self\":\"/v1/authors/4\"}},{\"id\":\"5\",\"type\":\"books\",\"attributes\":{\"date_published\":\"1998-07-02\",\"title\":\"Harry Potter and the Chamber of Secrets\",\"created_at\":\"2016-02-12 17:50:37\",\"updated_at\":\"2016-02-12 17:50:37\"},\"relationships\":{\"chapters\":{\"links\":{\"self\":\"/v1/authors/5/relationships/chapters\",\"related\":\"/v1/authors/5/chapters\"}},\"firstChapter\":{\"links\":{\"self\":\"/v1/authors/5/relationships/firstChapter\",\"related\":\"/v1/authors/5/firstChapter\"}},\"series\":{\"links\":{\"self\":\"/v1/authors/5/relationships/series\",\"related\":\"/v1/authors/5/series\"},\"data\":{\"id\":\"2\",\"type\":\"series\"}},\"author\":{\"links\":{\"self\":\"/v1/authors/5/relationships/author\",\"related\":\"/v1/authors/5/author\"},\"data\":{\"id\":\"2\",\"type\":\"authors\"}},\"stores\":{\"links\":{\"self\":\"/v1/authors/5/relationships/stores\",\"related\":\"/v1/authors/5/stores\"}},\"photos\":{\"links\":{\"self\":\"/v1/authors/5/relationships/photos\",\"related\":\"/v1/authors/5/photos\"}}},\"links\":{\"self\":\"/v1/authors/5\"}},{\"id\":\"6\",\"type\":\"books\",\"attributes\":{\"date_published\":\"1999-07-08\",\"title\":\"Harry Potter and the Prisoner of Azkaban\",\"created_at\":\"2016-02-12 17:50:37\",\"updated_at\":\"2016-02-12 17:50:37\"},\"relationships\":{\"chapters\":{\"links\":{\"self\":\"/v1/authors/6/relationships/chapters\",\"related\":\"/v1/authors/6/chapters\"}},\"firstChapter\":{\"links\":{\"self\":\"/v1/authors/6/relationships/firstChapter\",\"related\":\"/v1/authors/6/firstChapter\"}},\"series\":{\"links\":{\"self\":\"/v1/authors/6/relationships/series\",\"related\":\"/v1/authors/6/series\"},\"data\":{\"id\":\"2\",\"type\":\"series\"}},\"author\":{\"links\":{\"self\":\"/v1/authors/6/relationships/author\",\"related\":\"/v1/authors/6/author\"},\"data\":{\"id\":\"2\",\"type\":\"authors\"}},\"stores\":{\"links\":{\"self\":\"/v1/authors/6/relationships/stores\",\"related\":\"/v1/authors/6/stores\"}},\"photos\":{\"links\":{\"self\":\"/v1/authors/6/relationships/photos\",\"related\":\"/v1/authors/6/photos\"}}},\"links\":{\"self\":\"/v1/authors/6\"}},{\"id\":\"7\",\"type\":\"books\",\"attributes\":{\"date_published\":\"2000-07-08\",\"title\":\"Harry Potter and the Goblet of Fire\",\"created_at\":\"2016-02-12 17:50:37\",\"updated_at\":\"2016-02-12 17:50:37\"},\"relationships\":{\"chapters\":{\"links\":{\"self\":\"/v1/authors/7/relationships/chapters\",\"related\":\"/v1/authors/7/chapters\"}},\"firstChapter\":{\"links\":{\"self\":\"/v1/authors/7/relationships/firstChapter\",\"related\":\"/v1/authors/7/firstChapter\"}},\"series\":{\"links\":{\"self\":\"/v1/authors/7/relationships/series\",\"related\":\"/v1/authors/7/series\"},\"data\":{\"id\":\"2\",\"type\":\"series\"}},\"author\":{\"links\":{\"self\":\"/v1/authors/7/relationships/author\",\"related\":\"/v1/authors/7/author\"},\"data\":{\"id\":\"2\",\"type\":\"authors\"}},\"stores\":{\"links\":{\"self\":\"/v1/authors/7/relationships/stores\",\"related\":\"/v1/authors/7/stores\"}},\"photos\":{\"links\":{\"self\":\"/v1/authors/7/relationships/photos\",\"related\":\"/v1/authors/7/photos\"}}},\"links\":{\"self\":\"/v1/authors/7\"}},{\"id\":\"8\",\"type\":\"books\",\"attributes\":{\"date_published\":\"2003-06-21\",\"title\":\"Harry Potter and the Order of the Phoenix\",\"created_at\":\"2016-02-12 17:50:37\",\"updated_at\":\"2016-02-12 17:50:37\"},\"relationships\":{\"chapters\":{\"links\":{\"self\":\"/v1/authors/8/relationships/chapters\",\"related\":\"/v1/authors/8/chapters\"}},\"firstChapter\":{\"links\":{\"self\":\"/v1/authors/8/relationships/firstChapter\",\"related\":\"/v1/authors/8/firstChapter\"}},\"series\":{\"links\":{\"self\":\"/v1/authors/8/relationships/series\",\"related\":\"/v1/authors/8/series\"},\"data\":{\"id\":\"2\",\"type\":\"series\"}},\"author\":{\"links\":{\"self\":\"/v1/authors/8/relationships/author\",\"related\":\"/v1/authors/8/author\"},\"data\":{\"id\":\"2\",\"type\":\"authors\"}},\"stores\":{\"links\":{\"self\":\"/v1/authors/8/relationships/stores\",\"related\":\"/v1/authors/8/stores\"}},\"photos\":{\"links\":{\"self\":\"/v1/authors/8/relationships/photos\",\"related\":\"/v1/authors/8/photos\"}}},\"links\":{\"self\":\"/v1/authors/8\"}},{\"id\":\"9\",\"type\":\"books\",\"attributes\":{\"date_published\":\"2005-07-16\",\"title\":\"Harry Potter and the Half-Blood Prince\",\"created_at\":\"2016-02-12 17:50:37\",\"updated_at\":\"2016-02-12 17:50:37\"},\"relationships\":{\"chapters\":{\"links\":{\"self\":\"/v1/authors/9/relationships/chapters\",\"related\":\"/v1/authors/9/chapters\"}},\"firstChapter\":{\"links\":{\"self\":\"/v1/authors/9/relationships/firstChapter\",\"related\":\"/v1/authors/9/firstChapter\"}},\"series\":{\"links\":{\"self\":\"/v1/authors/9/relationships/series\",\"related\":\"/v1/authors/9/series\"},\"data\":{\"id\":\"2\",\"type\":\"series\"}},\"author\":{\"links\":{\"self\":\"/v1/authors/9/relationships/author\",\"related\":\"/v1/authors/9/author\"},\"data\":{\"id\":\"2\",\"type\":\"authors\"}},\"stores\":{\"links\":{\"self\":\"/v1/authors/9/relationships/stores\",\"related\":\"/v1/authors/9/stores\"}},\"photos\":{\"links\":{\"self\":\"/v1/authors/9/relationships/photos\",\"related\":\"/v1/authors/9/photos\"}}},\"links\":{\"self\":\"/v1/authors/9\"}},{\"id\":\"10\",\"type\":\"books\",\"attributes\":{\"date_published\":\"2007-07-21\",\"title\":\"Harry Potter and the Deathly Hallows\",\"created_at\":\"2016-02-12 17:50:37\",\"updated_at\":\"2016-02-12 17:50:37\"},\"relationships\":{\"chapters\":{\"links\":{\"self\":\"/v1/authors/10/relationships/chapters\",\"related\":\"/v1/authors/10/chapters\"}},\"firstChapter\":{\"links\":{\"self\":\"/v1/authors/10/relationships/firstChapter\",\"related\":\"/v1/authors/10/firstChapter\"}},\"series\":{\"links\":{\"self\":\"/v1/authors/10/relationships/series\",\"related\":\"/v1/authors/10/series\"},\"data\":{\"id\":\"2\",\"type\":\"series\"}},\"author\":{\"links\":{\"self\":\"/v1/authors/10/relationships/author\",\"related\":\"/v1/authors/10/author\"},\"data\":{\"id\":\"2\",\"type\":\"authors\"}},\"stores\":{\"links\":{\"self\":\"/v1/authors/10/relationships/stores\",\"related\":\"/v1/authors/10/stores\"}},\"photos\":{\"links\":{\"self\":\"/v1/authors/10/relationships/photos\",\"related\":\"/v1/authors/10/photos\"}}},\"links\":{\"self\":\"/v1/authors/10\"}}]";

    private Map<String, Map<Integer, JsonObject>> includedMap;

    @Before
    public void init() {
        includedMap = IncludedUtil.parseIncluded(Json.parse(JSONAPI_INCLUDED_ARRAY).asArray());
    }

    @Test
    public void testParseArray() {
        JsonValue testArray = Json.parse(JSONAPI_RELATIONSHIP_DATA_ARRAY_BOOKS);
        Collection<Book> books = RelationshipUtil.parseArray(testArray.asArray(), Book.class, new RelationshipFieldParser.IncludedLookupListener() {
            @Override
            public JsonObject lookup(String type, int id) {
                if(includedMap.containsKey(type) && includedMap.get(type).containsKey(id)) {
                    return includedMap.get(type).get(id);
                }else {
                    return null;
                }
            }
        });

        assertNotNull(books);

        Iterator<Book> iter = books.iterator();

        assertTrue(books.size() == 4);
        assertTrue(iter.next().title.equals("The Fellowship of the Ring"));
        assertTrue(iter.next().title.equals("The Two Towers"));
        assertTrue(iter.next().title.equals("Return of the King"));
        assertTrue(iter.next().title.equals("The Hobbit"));
    }

    @Test
    public void testParseObject() {
        JsonValue testObject = Json.parse(JSONAPI_RELATIONSHIP_DATA_OBJECT_BOOK);
        Book book = RelationshipUtil.parseObject(testObject.asObject(), Book.class, new RelationshipFieldParser.IncludedLookupListener() {
            @Override
            public JsonObject lookup(String type, int id) {
                if(includedMap.containsKey(type) && includedMap.get(type).containsKey(id)) {
                    return includedMap.get(type).get(id);
                }else {
                    return null;
                }
            }
        });

        assertNotNull(book);
        assertTrue(book.title.equals("The Fellowship of the Ring"));
    }
}
