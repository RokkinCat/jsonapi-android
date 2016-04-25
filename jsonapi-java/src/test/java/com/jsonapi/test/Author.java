package com.jsonapi.test;

import com.jsonapi.annotations.JsonApiName;
import com.jsonapi.annotations.Link;
import com.jsonapi.annotations.Relationship;

import java.util.List;

/**
 * Created by greg on 2/1/16.
 */
public class Author {
    String name;

    @JsonApiName("date_of_birth")
    String dob;

    @JsonApiName("date_of_death")
    String dod;

    String created_at;
    String updated_at;
    int age;

    @Relationship("books")
    List<Book> books;

    @Link("self")
    String self;

    @Link("related")
    String related;

    public String toString() {
        String retVal = "";
        if(books != null) {
            for (Book book : books) {
                retVal += "\t" + book.toString() + "\n";
            }
        }
        return  "NAME: " + name + "\n" +
                "DOB: " + dob + "\n" +
                "DOD: " + dod + "\n" +
                "CREATED_AT: " + created_at + "\n" +
                "UPDATED_AT: " + updated_at + "\n" +
                "BOOKS: " + retVal;
    }
}
