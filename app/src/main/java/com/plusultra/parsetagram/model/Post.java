package com.plusultra.parsetagram.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Post")
public class Post extends ParseObject{
    private static final String KEY_DESCRIPTION = "desrcitption";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_USER = "user";
}
