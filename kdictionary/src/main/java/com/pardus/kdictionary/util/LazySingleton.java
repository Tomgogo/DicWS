package com.pardus.kdictionary.util;

/**
 * Created by Tom on 2015/6/23.
 */
public class LazySingleton {
    private LazySingleton instance;
    private LazySingleton(){}

    public LazySingleton getInstance() {
        if (instance == null) {
            synchronized (LazySingleton.class) {
                instance = new LazySingleton();
            }
        }
        return instance;
    }
}
