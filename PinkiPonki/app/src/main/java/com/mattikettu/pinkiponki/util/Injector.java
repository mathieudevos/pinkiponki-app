package com.mattikettu.pinkiponki.util;

/**
 * Created by mathieu on 10/06/2016.
 */

import dagger.ObjectGraph;

public final class Injector {

    private static ObjectGraph objectGraph = null;

    public static void init(final Object rootModule) {

        if (objectGraph == null) {
            objectGraph = ObjectGraph.create(rootModule);
        } else {
            //Init now happens on Application and not on activity, so cannot happen twice.
            objectGraph = objectGraph.plus(rootModule);
        }

        // Inject statics
        objectGraph.injectStatics();

    }

    public static void init(final Object rootModule, final Object target) {
        init(rootModule);
        inject(target);
    }

    public static void inject(final Object target) {
        objectGraph.inject(target);
    }

    public static <T> T resolve(Class<T> type) {
        return objectGraph.get(type);
    }
}
