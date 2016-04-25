package com.jsonapi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by greg on 2/9/16.
 */
@Target(ElementType.FIELD) @Retention(RetentionPolicy.RUNTIME)
public @interface Relationship {
    String value();
}
