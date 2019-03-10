package com.example.mikhaelalayon.appremn.arduino;

import android.content.Context;

import java.util.HashSet;
import java.util.Set;

public class ArduinoInterface {

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
        listeners = new HashSet<>();
    }

    public void initialize(Context ctx) {
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
