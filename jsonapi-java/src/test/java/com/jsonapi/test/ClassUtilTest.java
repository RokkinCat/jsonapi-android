package com.jsonapi.test;

import com.jsonapi.util.ClassUtil;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Created by greg on 2/12/16.
 */
public class ClassUtilTest {

    @Test
    public void testMapFields() {
        Map<String, Field> fields = ClassUtil.mapRelationshipFields(Author.class);

        assertNotNull(fields);
        assertTrue(fields.containsKey("books"));
    }
}
