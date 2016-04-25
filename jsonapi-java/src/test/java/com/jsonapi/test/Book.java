package com.jsonapi.test;

import com.jsonapi.annotations.Link;
import com.jsonapi.annotations.Relationship;

/**
 * Created by greg on 2/9/16.
 */
public class Book {
    String title;

    @Relationship("author")
    Author author;

    @Link("self")
    String self;

    @Link("related")
    String related;

    @Override
    public String toString() {
        return this.title;
    }
}
