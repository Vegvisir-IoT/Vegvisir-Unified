package com.vegvisir.app.annotativemap;

import android.app.Application;

import java.util.concurrent.ConcurrentHashMap;

public class AnnotativeMapApplication extends Application {

    private ConcurrentHashMap<Coordinates, Annotation> annotations = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Coordinates, Annotation> getAnnotations() {
        return annotations;
    }
}
