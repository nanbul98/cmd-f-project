package com.example.mikhaelalayon.appremn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mikhaelalayon.appremn.arduino.ArduinoInterface;


public class MainActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG = "debug";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String GET_POINTS = "get points";
    private static final int POINT_AMOUNT = 10;
    public static final String POINT = "pointKey";

    private int points;
    private TextView displayPoint;

    ImageButton imageButton;

    private ArduinoInterface arduino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arduino = ArduinoInterface.getInstance();
        arduino.addListener(new ArduinoInterface.ArduinoListener() {
            @Override
            public void onDistanceUpdated(int distance) {
                incrementPoint();
            }
        });

        sharedPreferences = getSharedPreferences(POINT, Context.MODE_PRIVATE);
        sharedPreferences.getInt(POINT, 0);
        displayPoint = findViewById(R.id.textView2);

        displayPoint.setText("Points: " + points);

        imageButton = findViewById(R.id.take_image);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(MainActivity.this, ImageCapture.class);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (data.getIntExtra(GET_POINTS, 0) > 0) {
                incrementPoint();
            }
        }
    }

    private void incrementPoint() {
        Log.i(TAG, "called incrementPoint");
        sharedPreferences.getInt(POINT, 0);
        points += POINT_AMOUNT;
        editor = sharedPreferences.edit();
        editor.putInt(POINT, points);
        editor.commit();
        displayPoint.setText("Points: " + points);
    }

}