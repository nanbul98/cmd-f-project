package com.example.mikhaelalayon.appremn.arduino;

import android.content.Context;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

public class ArduinoInterface {

    private static final String TAG = "arduino-interface";

    private static ArduinoInterface instance = null;

    public static ArduinoInterface getInstance() {
        if (instance == null) {
            instance = new ArduinoInterface();
        }
        return instance;
    }

    private Set<ArduinoListener> listeners;
    private ArduinoStuff stuff;

    private ArduinoInterface() {
        Log.i(TAG, "created instance");
        listeners = new HashSet<>();
    }

    public void initialize(Context ctx) {
        Log.i(TAG, "running init");
        stuff = new ArduinoStuff(this, ctx);
    }

    public void addListener(ArduinoListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ArduinoListener listener) {
        listeners.remove(listener);
    }

    protected void update(int dist) {
        for (ArduinoListener l : listeners) {
            l.onDistanceUpdated(dist);
        }
    }

    public interface ArduinoListener {

        void onDistanceUpdated(int distance);

    }

}