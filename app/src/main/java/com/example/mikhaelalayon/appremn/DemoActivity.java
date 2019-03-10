package com.example.mikhaelalayon.appremn;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.mikhaelalayon.appremn.arduino.ArduinoInterface;

/**
 * Activity for displaying available Bluetooth LE devices.
 */
public class DemoActivity extends AppCompatActivity implements ArduinoInterface.ArduinoListener {

    private TextView content;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArduinoInterface arduino = ArduinoInterface.getInstance();
        arduino.initialize(this);
        arduino.addListener(this);

        content = findViewById(R.id.content);
    }

    @Override
    public void onDistanceUpdated(int distance) {
        content.setText("Distance: " + String.valueOf(distance));
    }
}