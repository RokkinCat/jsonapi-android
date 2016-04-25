package com.jsonapi.test;

import com.jsonapi.annotations.JsonApiName;

/**
 * Created by greg on 2/12/16.
 */
public class Person {

    @JsonApiName("first-name")
    private String firstName;

    @JsonApiName("last-name")
    private String lastName;
    private String twitter;
}
